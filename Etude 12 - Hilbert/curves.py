import math, sys
from tkinter import Tk, Canvas, Frame, BOTH, Image,Label
from PIL import Image, ImageTk

class Hilcurve(Frame):

    def __init__(self, order, ratio, master):

        Frame.__init__(self, master)

        self.x = 530
        self.y = self.x

        self.n = int(order)
        self.ratio = float(ratio)

        self.line_length = 520


        self.length_array = [0] * int(math.pow(2, int(self.n)))
        self.order = []

        self.positions = ["D", "R", "U", "L"]

        self.colour = "black"

        self.offset = False
        self.width = 1.0



        self.canvas = Canvas(self)

        self.init_UI()

        self.normal()

        if self.n > 1:
            self.order_n(self.n)

        self.process()

        self.draw()

        self.save_image()


    def init_UI(self):
        self.master.title("Hilbert Curves")
        self.pack(fill=BOTH, expand=1)
        self.canvas = Canvas(self, width=self.x, height=self.y)

    def save_image(self):
        self.canvas.update()
        self.canvas.postscript(file="hilbert_curve.ps", colormode='color')

    def normal(self):
        self.order.append(self.positions[0])
        self.order.append(self.positions[1])
        self.order.append(self.positions[2])


    def order_n(self, n):
        for i in range(1, n):
            a = []
            b = []
            a.extend(self.order)
            b.extend(self.order)
            self.order.clear()

            a = self.rotate(a, True)
            self.order.extend(a)
            a.clear()

            self.order.append(self.positions[0])
            self.order.extend(b)
            self.order.append(self.positions[1])
            self.order.extend(b)
            self.order.append(self.positions[2])

            a.extend(b)
            a = self.rotate(a, False)
            self.order.extend(a)


    def rotate(self,order, right=False):

        for idx, item in enumerate(order):
            #rotate right
            if right:
                if "R" in item:
                    order[idx] = "D"
                    continue

                if "D" in item:
                    order[idx] = "R"
                    continue

                if "L" in item:
                    order[idx] = "U"
                    continue

                if "U" in item:
                    order[idx] = "L"
             #rotate left
            else:
                if "R" in item:
                    order[idx] = "U"
                    continue

                if "D" in item:
                    order[idx] = "L"
                    continue

                if "L" in item:
                    order[idx] = "D"
                    continue

                if "U" in item:
                    order[idx] = "R"

        return order

    def process(self):

        size = int(math.pow(2, int(self.n)))

        if self.n == 1:
            self.length_array[1] = self.line_length
            return

        ratio_total = 0.0
        mid_point = int(math.pow(2, self.n-1))
        current_order_mid_point = mid_point
        current_order = 1

        for order in range(current_order, self.n):
            current_order_mid_point = int(current_order_mid_point/2)
            current_order_ratio = math.pow(self.ratio, order) * current_order_mid_point
            ratio_total += current_order_ratio

        x = mid_point + ratio_total

        order_size = int((self.line_length)/x)

        if self.n == 6 and self.ratio == 2:
            order_size = 3

        if self.n == 7 and self.ratio >= 2:
            order_size = 1.45

        if self.n > 9:
            order_size = 1.0


        if self.n == 6 and self.ratio < 1:
            self.offset = True
            order_size = 11.5

        self.length_array[1:size] = [order_size] * size

        for current_order in range(2, self.n+1):

            mid = int(math.pow(2, current_order))
            index = int(mid/2)

            self.length_array[index] = int(order_size * math.pow(self.ratio, current_order - 1))
            index += mid
            if index == mid:
                break
            else:
                while index <= size:
                    self.length_array[index] = int(order_size * self.ratio)
                    index = int(index + mid)

        self.length_array = self.length_array[0:mid_point + 1] + self.length_array[1:mid_point]

        for inx, item in enumerate(self.length_array, start=1):
            if inx >= len(self.length_array):
                break
            self.length_array[inx] = self.length_array[inx] + self.length_array[inx - 1]

    def get_offset(self):
        if 1 <= self.n <= 4:
            if self.n == 3 and self.ratio > 3:
                return 10
            if self.n == 4 and self.ratio >= 4:
                return 20
            return 5
        if self.n == 5:
            if self.ratio < 1:
                return 3
            if self.ratio > 2:
                return 45
            if self.ratio > 1:
                return 25
            return 15
        if 6 <= self.n <= 9:
            return 10

        if self.n > 9:
            return 5

    def draw(self):

        x_index = 0
        y_index = 0

        line_count = 1

        for a in self.order:

            self.colour = "black"

            if line_count % int(math.pow(4, self.n-1)) == 0 and self.n != 1:
                self.colour = "red"
            line_count += 1
            if a == "L":
                self.draw_left(x_index,y_index)
                x_index -= 1
            if a == "R":
                self.draw_right(x_index,y_index)
                x_index += 1
            if a == "D":
                self.draw_down(x_index,y_index)
                y_index += 1
            if a == "U":
                self.draw_up(x_index,y_index)
                y_index -= 1

        self.canvas.pack()

    def draw_left(self, x_index, y_index):

        x = self.length_array[x_index]
        x2 = self.length_array[x_index - 1]
        y = self.length_array[y_index]
        y2 = y

        self.draw_line(x, y, x2, y2)

    def draw_right(self, x_index, y_index):
        x = self.length_array[x_index]
        y = self.length_array[y_index]
        x2 = self.length_array[x_index + 1]
        y2 = y

        self.draw_line(x, y, x2, y2)

    def draw_down(self, x_index, y_index):
        x = self.length_array[x_index]
        x2 = x
        y = self.length_array[y_index]
        y2 = self.length_array[y_index + 1]

        self.draw_line(x, y, x2, y2)

    def draw_up(self, x_index, y_index):

        x = self.length_array[x_index]
        x2 = x
        y = self.length_array[y_index]
        y2 = self.length_array[y_index - 1]

        self.draw_line(x, y, x2, y2)

    def draw_line(self, x, y, x2, y2):

        if self.offset == False:
            offset = self.get_offset()
        else:
            offset = self.get_ratio_offset()

        self.canvas.create_line(x + offset, y + offset, x2 + offset, y2 + offset, fill=self.colour, width=self.width)

    def get_ratio_offset(self):
        if self.n == 6 and self.ratio < 1:
            return 15
        if self.n == 7 and self.ratio > 1:
            return 25

#resizing/scaling with window
class new_frame(Frame):
    def __init__(self, master, *pargs):
        Frame.__init__(self, master, *pargs)


        self.image = Image.open("./hilbert_curve.ps")
        self.img_copy= self.image.copy()

        self.background_image = ImageTk.PhotoImage(self.image)

        self.background = Label(self, image=self.background_image)
        self.background.pack(fill=BOTH, expand=1)
        self.background.bind('<Configure>', self._resize_image)

    def _resize_image(self,event):

        new_width = event.width
        new_height = event.height

        self.image = self.img_copy.resize((new_width, new_height))

        self.background_image = ImageTk.PhotoImage(self.image)
        self.background.configure(image =  self.background_image)

def main():

    order = None
    ratio = None

    if len(sys.argv) < 2:
        print("Order missing!")
        return

    if len(sys.argv) > 2:
        ratio = float(sys.argv[2])
    else:
        ratio = 1.0

    if int(sys.argv[1]) >= 1:
        order = int(sys.argv[1])
    else:
        print('Order must be higher >= 1!')
        return

    if order == 1 and (ratio < 1 or ratio > 1):
        print("Ratio can't be applied to order 1 curves!")
        return

    root = Tk()
    root.geometry("530x530")
    ex = Hilcurve(order, ratio, root)
    root.update()
    root.after(1000, root.destroy)
    root.mainloop()

    root = Tk()
    root.title("Hilbert Curves")
    root.geometry("530x530")
    root.configure(background="black")
    e = new_frame(root)
    e.pack(fill=BOTH, expand=1)
    root.mainloop()






if __name__ == '__main__':
    main()
