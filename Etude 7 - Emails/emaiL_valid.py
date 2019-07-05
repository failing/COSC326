import re
import sys
from string import punctuation

ip_regex = r"^(.*?)@\[(([01]?\d\d?|2[0-4]\d|25[0-5]).){3}([01]?\d\d?|2[0-4]\d|25[0-5])\]$"
email_regex = r"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
valid_extensions = [".co.nz",".com.au",".co.ca",".co.us",".co.uk",".com"]
punctuation_list = punctuation
dot = "_dot_"
at = "_at_"

def ip_check(email):
    match = re.match(ip_regex, email)
    
    if match is not None and email.endswith("]"):
        return True
    return False

def extension_check(email):
    for ext in valid_extensions:
        if email.endswith(ext):
            return True, ext #return the ext as well as it might be needed
    return False, False

def domain_check(email, ext):
    mailbox = email.split("@")
    domain = str(mailbox[1].split(ext)[0])

    for char in domain:
        for symbol in filter(lambda dot: dot!='.', punctuation_list):
            if symbol == char:
                return False
    return True

# https://stackoverflow.com/a/14496145
def replace_right(source, target, replacement, replacements=None):
    return replacement.join(source.rsplit(target, replacements))

def email_check(email):
    if re.match(email_regex, email) is not None:
        return True
    return False

def mailbox_check(mailbox):
    if mailbox.isalnum():
        return True
    else:
        if mailbox.startswith(tuple(punctuation_list)) or mailbox.endswith(tuple(punctuation_list)):
            return False
        else:
            for index, value in enumerate(mailbox):
                if value in list(set(punctuation_list) - set(["-",".","_"])):
                    return False
                if mailbox[index] in punctuation_list:
                    if mailbox[index+1] in punctuation_list:
                        return False
        return True
    return False

for email in sys.stdin:
    email = email.rstrip()
    old_email = email
    email = email.lower()

    if "@" not in email and at not in email:
        print("{0} <- no @ symbol".format(old_email))
        continue

    if at in email and "@" not in email:
        email = replace_right(email, at, "@", 1)

    if "@" not in email:
        print("{0} <- no @ symbol".format(old_email))
        continue

    email = email.replace(dot, ".")
    mailbox = email.split("@")[0]

    if mailbox_check(mailbox):
        if ip_check(email):
            validate = email.split(']')
            if validate[1] == "":
                print(email)
            else:
                print("{0} <- invalid email".format(old_email))
        else:
            boolean, ext = extension_check(email)
            if boolean:
                if domain_check(email, ext):
                    if email_check(email):
                        print(email)
                    else:
                        print("{0} <- invalid email".format(old_email))
                else:
                    print("{0} <- invalid domain".format(old_email))
            else:
                print("{0} <- invalid extension".format(old_email))
    else:
        print("{0} <- invalid email".format(old_email))


