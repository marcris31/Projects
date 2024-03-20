import threading
import time
from tkinter import *
import math
import tkinter as tk
from tkinter import filedialog
from tkinter import messagebox
from tkinter.tix import IMAGETEXT
import cv2

import numpy as np
from Gcode_toCoord import ConversionToC

from file_handler import FileHandler
from drawing_algorithms import DrawingAlgorithms
from simulation import Simulation
from conversion_toGcode import Conversion

class Interface(Frame):
    def __init__(self, root):
        super().__init__(root)
        self.root = root
        self.simulation = Simulation(self)
        self.setup_ui()
        self.root.bind('<Motion>', self.get_mouse_position)

        self.pack()

    def setup_ui(self):
        self.label = tk.Label(self.root, text="Welcome to CNC Flame Cutting Machine Simulator")
        self.label.pack(pady=10)

        self.canvas = tk.Canvas(self.root, width=1000, height=500, bg="white")
        self.canvas.pack(pady=10)

        # Mouse position label
        self.mouse_position_label = tk.Label(self.root, text="Mouse Position: X=0, Y=0")
        self.mouse_position_label.pack(side=tk.TOP, padx=5)

        # Start and Stop buttons
        self.start_button = tk.Button(self.root, text="Start", command=self.simulation.start)
        self.start_button.pack(side=tk.LEFT, padx=5)

        self.stop_button = tk.Button(self.root, text="Stop", command=self.simulation.stop, state=tk.DISABLED)
        self.stop_button.pack(side=tk.LEFT, padx=5)

        # Coordinate entry fields
        self.x_label = tk.Label(self.root, text="start X:")
        self.x_label.pack(side=tk.LEFT, padx=5)
        self.x_text = tk.Entry(self.root, width=10)
        self.x_text.pack(side=tk.LEFT, padx=5)

        self.y_label = tk.Label(self.root, text="start Y:")
        self.y_label.pack(side=tk.LEFT, padx=5)
        self.y_text = tk.Entry(self.root, width=10)
        self.y_text.pack(side=tk.LEFT, padx=5)

        self.x1_label = tk.Label(self.root, text="End X:")
        self.x1_label.pack(side=tk.LEFT, padx=5)
        self.x1_text = tk.Entry(self.root, width=10)
        self.x1_text.pack(side=tk.LEFT, padx=5)

        self.y1_label = tk.Label(self.root, text="End Y:")
        self.y1_label.pack(side=tk.LEFT, padx=5)
        self.y1_text = tk.Entry(self.root, width=10)
        self.y1_text.pack(side=tk.LEFT, padx=5)

        # self.arc_radius_label = tk.Label(self.root, text="Arc Radius:")
        # self.arc_radius_label.pack(side=tk.LEFT, padx=5)
        # self.arc_radius_text = tk.Entry(self.root, width=10)
        # self.arc_radius_text.pack(side=tk.LEFT, padx=5)

        self.generate_gcode_button = tk.Button(self.root, text="Generate G-Code", command=Conversion.generate_gcode)
        self.generate_gcode_button.pack(side=tk.LEFT, padx=5)

        # Read file and Clear canvas buttons
        self.read_file_button = tk.Button(self.root, text="Read file", command=self.read_file)
        self.read_file_button.pack(side=tk.LEFT, padx=5)

        self.clear_canvas_button = tk.Button(self.root, text="Clear canvas", command=self.clear_canvas)
        self.clear_canvas_button.pack(side=tk.RIGHT, padx=5)

        self.convert_gcode_button = tk.Button(self.root, text="Convert G-Code to Cartesian", command=self.convert_gcode_to_cartesian)
        self.convert_gcode_button.pack(side=tk.LEFT, padx=5)

        # Draw grid on the canvas
        self.draw_grid()

    # def start_simulation(self):
    #     # Assuming you have a Simulation object as part of the Interface class
    #     if not self.simulation:
    #         self.simulation = Simulation(self)
    #     self.simulation.start()

    # def stop_simulation(self):
    #     # Call the stop method on the simulation object
    #     if self.simulation:
    #         self.simulation.stop()

    def clear_canvas(self):
        self.canvas.delete("all")

    def read_file(self):
        file_path = FileHandler.open_file_dialog()
        if file_path:
            FileHandler.read_coordinates_from_file(file_path, self)

    def draw_grid(self):
        self.canvas.update_idletasks()  # Update canvas size
        grid_spacing = self.calculate_grid_spacing()
        for i in range(0, self.canvas.winfo_width(), grid_spacing):
            self.canvas.create_line(i, 0, i, self.canvas.winfo_height(), fill="pink")
        for i in range(0, self.canvas.winfo_height(), grid_spacing):
            self.canvas.create_line(0, i, self.canvas.winfo_width(), i, fill="pink")
        
    def calculate_grid_spacing(self):
        return 20

    #get mouse position
    def get_mouse_position(self, event):
        x = event.x
        y = event.y
        
        #daca x si y sunt in afara canvasului, nu se intampla nimic
        if x > 1000 or y > 500:
            return
        print(f"X:{x}, Y:{y}")
        self.mouse_position_label.config(text=f"Cutting head position: X={x}, Y={y}")
        
    def trigger_gcode_generation(self):
        # Open a dialog to let the user select the .txt file with drawing coordinates
        file_path = filedialog.askopenfilename(title="Select the drawing coordinates file", filetypes=[("Text files", "*.txt")])
        if file_path:
            output_file_path = filedialog.asksaveasfilename(title="Save G-Code as", defaultextension=".gcode", filetypes=[("G-Code files", "*.gcode"), ("All files", "*.*")])
            if output_file_path:
                # Call the G-code generation function from the conversion_toGcode module
                Conversion.generate_gcode_from_file(file_path, output_file_path)

                # Provide feedback that G-code file has been generated
                messagebox.showinfo("Success", f"G-Code generated and saved as {output_file_path}")   

    def convert_gcode_to_cartesian(self):
        input_file_path = filedialog.askopenfilename(title="Select G-Code File", filetypes=[("G-Code files", "*.gcode"), ("All files", "*.*")])
        if input_file_path:
            output_file_path = filedialog.asksaveasfilename(title="Save Cartesian Coordinates as", defaultextension=".txt", filetypes=[("Text files", "*.txt"), ("All files", "*.*")])
            if output_file_path:
                conversion_instance = ConversionToC()
                cartesian_coords = conversion_instance.gcode_to_cartesian(input_file_path)

                # Save the Cartesian coordinates to the output file
                with open(output_file_path, 'w') as file:
                    for coords in cartesian_coords:
                        line = f"X{coords.get('X', 0)} Y{coords.get('Y', 0)} Z{coords.get('Z', 0)}\n"
                        file.write(line)

                messagebox.showinfo("Success", f"Cartesian coordinates generated and saved as {output_file_path}")
