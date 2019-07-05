My program for Etude 2,

run using:
    python3 dates.py

Each line enter a date and it will either spit out the valid space tidied up 
or display that is incorrect and the reason for so.

The bulk of the work is done using regex. If a date fails the regex the date is sent to another method to see why the date wasn't properly validated.

Example:
	2 Mar 3000
	8 Apr 1956
	28-6/11 <- INVALID: Separators are invalid
	1//1//11 <- INVALID: Incorrect Date Format