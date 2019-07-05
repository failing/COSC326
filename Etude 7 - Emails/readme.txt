Python version used - Python 3.6.5


Can either be run in two ways:

For multiple emails:
	1) python3 email_valid.py < email_testing.txt

For single line input:
	2) python3 email_valid.py

Example expected output on the testing text file given:

brad@lol.com
a_$b@cs.com <- invalid email
a__b@lol.com <- invalid email
a--b@cs.com <- invalid email
adsad.sdadsa@lol.com
a-b-c-d@l.com
ab_-l@a.com <- invalid email
la@l.com_<- invalid extenstion
a_@l.com <- invalid email
123@123.com
123_123@lolc.com
a-$a@s.com <- invalid email
a_l@[123.123.123.123]
la.a@l.co.co.co.nz
mailbox@a.co.nz
mailbox@cs.co.us
mailbox@l.co.us
mail_box@domain.com
email_l@ex.l.com