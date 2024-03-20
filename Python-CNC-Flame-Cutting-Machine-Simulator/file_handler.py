import math
import tkinter as tk
from tkinter import filedialog
import numpy as np

from drawing_algorithms import DrawingAlgorithms

class FileHandler:
    @staticmethod
    def read_coordinates_from_file (file_path, interface_instance):
        try:
            with open(file_path, 'r') as file:
                lines = file.readlines()

                for line in lines:
                    coordinates = line.strip().split(',')
                    coordinates = [int(coordinate) for coordinate in coordinates]
                    
                    if len(coordinates) == 4:
                        #draw a line if there are 4 coordinates
                        DrawingAlgorithms.draw_line(interface_instance.canvas,*coordinates)
                    elif len(coordinates) == 5:
                        DrawingAlgorithms.draw_arc(interface_instance.canvas, *coordinates)
        except FileNotFoundError:
            print(f"File not found: {file_path}")
        except Exception as e:
            print(f"Error reading file: {e}")

    @staticmethod
    def open_file_dialog():
        return filedialog.askopenfilename(title="Select a file", filetypes=[("Text files", "*.txt")])
