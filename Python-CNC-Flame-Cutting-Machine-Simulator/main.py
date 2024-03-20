from tkinter import *
from interface import Interface
from conversion_toGcode import Conversion

# Creating the main window
root = Tk()
app = Interface(root)
root.mainloop()
