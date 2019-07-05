import re
import sys


class Dates:
    def __init__(self):
        self.MAXYEAR = 3000
        self.MINYEAR = 1753
        self.valid = ['/', '-', ' ']
        # https://stackoverflow.com/a/26972181 a edited version of this regex
        self.date_valid = r"^(?:(?:31(\/|-| )(?:0?[13578]|1[02]|(?:Jan|JAN|jan|Mar|MAR||Mar|May|MAY|may|Jul|JUL|jul|" \
                          r"Aug|AUG|aug|Oct|OCT|oct|Dec|DEC|dec)))\1|(?:(?:29|30)(\/|-| )(?:0?[1,3-9]|1[0-2]|(?:Jan|" \
                          r"JAN|jan|Mar|MAR||Mar|Apr|APR|apr|May|MAY|may|Jun|JUN|jun|Jul|JUL|jul|Aug|AUG|aug|Sep|SEP|" \
                          r"sep|Oct|OCT|oct|Nov|nov|NOV|Dec|DEC|dec))\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-| )" \
                          r"(?:0?2|(?:Feb|FEB|feb))\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?" \
                          r":16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-| )(?:(?:0?[1-9]|(?:Jan|JA" \
                          r"N|jan|Mar|MAR||Mar|Apr|Feb|FEB|feb|APR|apr|May|MAY|may|Jun|JUN|jun|Jul|JUL|jul|Aug|AUG|aug|Sep|SEP|" \
                          r"sep))|(?:1[0-2]|(?:Oct|OCT|oct|Nov|nov|NOV|Dec|DEC|dec)))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$"
        self.months = {
            "Jan": ["1", "01", "JAN", "jan", "Jan", "31"],
            "Feb": ["2", "02", "FEB", "feb", "Feb", "28"],
            "Mar": ["3", "03", "MAR", "mar", "Mar", "31"],
            "Apr": ["4", "04", "APR", "apr", "Apr", "30"],
            "May": ["5", "05", "MAY", "may", "May", "31"],
            "Jun": ["6", "06", "JUN", "jun", "Jun", "30"],
            "Jul": ["7", "07", "JUL", "jul", "Jul", "31"],
            "Aug": ["8", "08", "AUG", "aug", "Aug", "31"],
            "Sep": ["9", "09", "SEP", "sep", "Sep", "30"],
            "Oct": ["10", "10", "OCT", "oct", "Oct", "31"],
            "Nov": ["11", "11", "NOV", "nov", "Nov", "30"],
            "Dec": ["12", "12", "DEC", "dec", "Dec", "31"]
        }
        self.separator = None

    def get_month(self, v):
        for key, value in self.months.items():
            for t in value:
                if v == t:
                    return key

    def check_leap_year(self, date):
        date = date.split(self.separator)
        if self.get_month(date[1]) == "Feb":
            if int(date[0]) == 29:
                if int(date[2]) % 4 == 0 and int(date[2]) % 100 != 0 or int(date[2]) % 400 == 0:
                    return True
                return False

    def validate_month(self, line):
        if self.get_month(line) is not None:
            return True

        return False

    def get_failure(self, date):

        sep_count = sum(x in set(date) for x in self.valid)

        if sep_count > 1:
            return "INVALID: Separators are invalid"

        for sep_count in date:
            for b in self.valid:
                if sep_count == b:
                    self.separator = sep_count

        try:
            day = date.split(self.separator)[0]
            month = date.split(self.separator)[1]
            year = date.split(self.separator)[2]
        except Exception:
            return "INVALID: Incorrect Date Format"

        if len(date.split(self.separator)) > 3:
            return "INVALID: Incorrect Date Format"

        if not day.isdigit():
            return "INVALID: Incorrect Day"
        if len(day) > 2:
            return "INVALID: Incorrect Day"
        if int(day) < 1:
            return "INVALID: Incorrect Day"

        if not year.isdigit():
            return "INVALID: Incorrect Year"
        if not year.isdigit():
            return "INVALID: Incorrect Year"

        if month.isdigit():
            if int(month) < 1:
                return "INVALID: Incorrect Month"
            if len(month) > 2:
                return "INVALID: Incorrect Month"
            if int(month) > 12:
                return "INVALID: Incorrect Month"
            if int(self.months.get(self.get_month(month))[0]) is None:
                return "INVALID: Incorrect Month"
        else:
            if self.months.get(self.get_month(month)) is None:
                return "INVALID: Incorrect Month"
        if self.get_month(month) == "Feb":
            if not self.check_leap_year(date) and int(day) == 29:
                return "INVALID: Not A Valid Leap Year Date"
        if int(day) != int(self.months.get(self.get_month(month))[5]):
            return "INVALID: Wrong Number Of Days For That Month"
        if int(year) < 10:
            return "INVALID: Year must be in yy or yyyy format"

        if int(year) < self.MINYEAR:
            return "INVALID: Year Out Of Range"

        if int(year) > self.MAXYEAR:
            return "INVALID: Year Out Of Range"

        if not self.validate_month(month) or int(month) > 12:
            return "INVALID: Not A Valid Month"

    def validate_date(self, line):

        match = re.match(self.date_valid, line)
        if match is not None:

            for seps in match.groups():
                if seps is not None:
                    self.separator = seps

            year = int(line.split(self.separator)[2])

            if year > self.MAXYEAR:
                return False, "INVALID: Year Out Of Range"

            if year < self.MINYEAR:
                if year >= 0 and year <= 49:
                    return True, 20 * 100 + year

                if year >= 50 and year <= 99:
                    return True, 19 * 100 + year

                return False, None
            return True, None
        return False, None

    def print_bad_date(self, date, error):
        print("{0} <- {1}".format(date, error))

    def print_format_date(self, date, day=None, month=None, year=None):

        d = date.split(self.separator)[0]
        m = self.get_month(date.split(self.separator)[1])
        y = date.split(self.separator)[2]

        if day is not None:
            d = day
        elif month is not None:
            m = self.get_month(month)
        elif year is not None:
            y = year

        print("{0} {1} {2}".format(d, m, y))


def main():

    date_validator = Dates()

    for line in sys.stdin:
        line = line.rstrip()

        return_code, error_code = date_validator.validate_date(line)

        if not return_code:
            if error_code is None:
                date_validator.print_bad_date(line, date_validator.get_failure(line))
            else:
                date_validator.print_bad_date(line, error_code)
        else:
            if error_code is None:
                date_validator.print_format_date(line)
            else:
                date_validator.print_format_date(line, year=error_code)


if __name__ == '__main__':
    main()
