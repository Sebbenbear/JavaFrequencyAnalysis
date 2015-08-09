//
//  main.c
//  FrequencyAnalysis
//
//  Created by Pauline Kelly on 2/08/15.
//  Copyright (c) 2015 Pauline Kelly. All rights reserved.
//

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#define TEXT_SIZE 200000  // Note, the longer the text the more likely you will get a good 'decode' from the start.
#define ALEN 26         // Number of chars in ENGLISH alphabet
#define CHFREQ "ETAONRISHDLFCMUGYPWBVKJXQZ" // Characters in order of appearance in English documents.
#define ALPHABET "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

typedef char bool;
typedef char CharHashMap[ALEN];

/* Program developed for NWEN243, Victoria University of Wellington
 Author: Kris Bubendorfer, this extended version (c) 2015
 LAB: 2
 
 This program applies a basic frequency analysis on a cyphertext.  It has been extened over the 2014 version to
 solve polyalphabetic cyphers - by brute force.  In this case, it applies the frequency analysis for different
 numbers of n keys (polyalphabetic Caeser).  Obviously it will need a cypher of about n times
 the typical length for a monoalphabetic cypher.
 
 Program is used like this:
 
 Compile:  gcc -o crack crack.c
 
 Test file (ctext): JWRLS, XSSH PZK JH HES BJFV, UZU (this is not a realistic length piece of cypher text)
 
 crack n
 
 Argument:
 
 n number of keys to try
 
 ---
 
 % cat ctext | crack 1
 ALICE, MEET YOU AT THE PARK, BOB   <-- of course it won't be this correct.  Don't worry about that for the -d option.
 AMFDE, UEET LNH AT TIE RASC, ONO   <-- this is what it really looks like, a larger sample is better, this is short.
 
 
 */

char upcase(char ch){
	if(islower(ch))
		ch -= 'a' - 'A';
	return ch;
}

void processFile(char *text, int n);

int main(int argc, char **argv){
	
	// first allocate some space for our input text (we will read from stdin).
	
	char* text = (char*)calloc(TEXT_SIZE+1, sizeof(char));
	char ch;
	int n, i;
	
	if(argc > 1 && (n = atoi(argv[1])) > 0); else{ fprintf(stderr,"Malformed argument, use: crack [n], n > 0\n"); exit(-1);} // get the command line argument n
	
	// Now read TEXT_SIZE or feof worth of characters (whichever is smaller) and convert to uppercase as we do it.
	// Added: changed to count frequencies as we read it in
	
	for(i = 0, ch = fgetc(stdin); i < TEXT_SIZE && !feof(stdin); i++, ch = fgetc(stdin)){
		text[i] = (ch = (isalpha(ch)?upcase(ch):ch));
	}
	
	/* At this point we have two things,s
	 *   1. The input cyphertext in "text"
	 *   2. The maximum number of keys to try (n) - we'll be trying 1..n keys.
	 *
	 * What you need to do is as follows:
	 *   1. create a for-loop that will check key lengths from 1..n
	 *   2. for each i <= n, spit the cypher text into i sub-texts.  For i = 1, 1 subtext, for i = 2, 2 subtexts, of alternating characters etc.
	 *   3. for each subtext:
	 *          a. count the occurance of each letter
	 *          b. then map this onto the CHFREQ, to create a map between the sub-text and english
	 *          c. apply the new map to the subtext
	 *   4. merge the subtexts
	 *   5. output the 'possibly' partially decoded text to stdout.  This will only look OK if i was the correct number of keys
	 *
	 * what you need to output (sample will be provided) - exactly:
	 * i maps -> stderr
	 * i 'possible' translations
	 *
	 * You would be wise to make seperate functions that perform various sub-tasks, and test them incrementally.  Any other approach will likely
	 * make your brain revolt.  This isn't a long program, mine is 160 lines, with comments (and written in a very verbose style) - if yours is
	 * getting too long, double check you're on the right track.
	 *
	 */
	
	processFile(text, n);
}

void initializeSubTexts(char **subTextStrings, char *text, size_t text_length, size_t sub_text_length, int i) {
	for(int stringIndex = 0; stringIndex < i; ++stringIndex){
		subTextStrings[stringIndex] = calloc(
											 sub_text_length, // Number of items
											 sizeof(char) // Size of each item
											 );
	}
	
	int appendIndex = 0;
	int stringIndex = 0;
	for(int index = 0; index < text_length; ++index){  //go through text
		subTextStrings[stringIndex][appendIndex] = text[index];
		
		stringIndex++;
		
		if(stringIndex == i) {
			stringIndex = 0;
			appendIndex++;
		}
	}
}
//Merge the subtexts back into the text
char *mergeSubTexts(char **subTextStrings, size_t text_length, int i) {
	char *newTextString = calloc(text_length+1, sizeof(char));
	
	int appendIndex = 0;
	int stringIndex = 0;
	for(int index = 0; index < text_length; ++index){  //go through text
		newTextString[index] = subTextStrings[stringIndex][appendIndex];
		
		stringIndex++;
		
		if(stringIndex == i) {
			stringIndex = 0;
			appendIndex++;
		}
	}
	
	return newTextString;
}

void freeSubTexts(char **subTextStrings, int i) {
	for(int stringIndex = 0; stringIndex < i; ++stringIndex){
		free(subTextStrings[stringIndex]);
	}
}

bool isLetter(char c) {
	return 'A' <= c && c <= 'Z';
}

int frequencyOf(int *frequencyTable, char c) {
	return frequencyTable[c - 'A'];
}

void initializeFrequencyTable(int *frequencyTable, char *text, size_t text_length) {
	// Zero the table
	bzero(frequencyTable, sizeof(int) * ALEN);
	
	for(int index = 0; index < text_length; ++index){ //go through subtext chars
		char c = text[index];					//get the current char
		if(isLetter(c)){								//if its alphabetic
			frequencyTable[c-'A']++;
		}
	}
}

int frequency_comparator(void *context, const void *a, const void *b) {
	int *frequencyTable = (int *)context;
	
	char char1 = *((const char *)a);
	char char2 = *((const char *)b);
	
	return frequencyOf(frequencyTable, char2) - frequencyOf(frequencyTable, char1);
}

void initializeSortedChars(char *sortedChars, int *frequencyTable) {
	memcpy(sortedChars, ALPHABET, sizeof(char) * ALEN);
	
	qsort_r(sortedChars, ALEN, sizeof(char), frequencyTable, frequency_comparator);
}

char CharHashMapGet(CharHashMap hash_map, char key) {
	return hash_map[key - 'A'];
}

void CharHashMapSet(CharHashMap hash_map, char key, char value) {
	hash_map[key - 'A'] = value;
}

void processFile(char *text, int n) {
	size_t text_length = strlen(text);
	
	for(int i = 1; i <= n; ++i){
		size_t sub_text_length = text_length/i + 2;//accounts for null terminator and rounding errors on divide
		
		//split cypher text into i subtexts
		char *subTextStrings[i];
		
		initializeSubTexts(subTextStrings, text, text_length, sub_text_length, i);
		
		//Do frequency analysis
		//for each character in the substring, get its count and store it  -- move
		
		for(int stringIndex = 0; stringIndex < i; ++stringIndex){  //for each substring
			
			//get the next substring
			char *currentSubtext = subTextStrings[stringIndex];
			size_t currentSubtext_length = strlen(currentSubtext);
			
			int frequencyTable[ALEN];
			initializeFrequencyTable(frequencyTable, currentSubtext, currentSubtext_length);
			
			//Now sort them based on frequency
			char sortedChars[ALEN+1];
			sortedChars[ALEN] = '\0';
			
			initializeSortedChars(sortedChars, frequencyTable);
			
			//now map our most frequent characters to the English languages most frequent characters
			CharHashMap characterMap;
			for(int i=0; i<ALEN; i++) {
				char ith_most_frequent_cypertext_char = sortedChars[i];
				char ith_most_frequent_english_char = CHFREQ[i];
				
				CharHashMapSet(characterMap, ith_most_frequent_cypertext_char, ith_most_frequent_english_char);
			}
			
			//Now apply the most characters to that of
			for(int i = 0; i < currentSubtext_length; ++i){
				char c = currentSubtext[i];
				if(isLetter(c)) {
					currentSubtext[i] = CharHashMapGet(characterMap, c);  //get the corresponding english value
				}
			}
		}
		
		//now merge the substrings back together
		char *result = mergeSubTexts(subTextStrings, text_length, i);
		printf("%s\n", result);
		free(result);
		
		freeSubTexts(subTextStrings, i);
	}
}
