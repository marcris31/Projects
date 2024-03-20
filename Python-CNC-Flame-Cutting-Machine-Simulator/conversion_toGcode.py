import tkinter as tk
import math
from tkinter import filedialog, messagebox

class Conversion:
    def generate_gcode():
        file_path = filedialog.askopenfilename(title="Select the drawing coordinates file", filetypes=[("Text files", "*.txt")])
        if file_path:
            output_file_path = filedialog.asksaveasfilename(title="Save G-Code as", defaultextension=".gcode", filetypes=[("G-Code files", "*.gcode"), ("All files", "*.*")])
            if output_file_path:
                # Read 
                with open(file_path, 'r') as file:
                    lines = file.readlines()
                
                # Convert the coordinates to G-code
                coordinates_list = [tuple(map(float, line.strip().split(','))) for line in lines]
                Conversion.convert_to_gcode(coordinates_list, output_file_path)

                # feedback 
                messagebox.showinfo("Success", f"G-Code generated and saved as {output_file_path}")

    def generate_gcode_from_file(input_file_path, output_file_path):
        # Read
        with open(input_file_path, 'r') as file:
            lines = file.readlines()
        
        # Convert 
        coordinates_list = [tuple(map(float, line.strip().split(','))) for line in lines]
        Conversion.convert_to_gcode(coordinates_list, output_file_path)

    def convert_to_gcode(coordinates_list, filename="output.gcode"):
        with open(filename, 'w') as gcode_file:
            gcode_file.write("%\n")  # Program start
            gcode_file.write("G21 ; Set units to millimeters\n")  # Assuming millimeters for units
            gcode_file.write("G90 ; Use absolute positioning\n")  # Absolute positioning
            
            for coords in coordinates_list:
                if len(coords) == 4:  # Line
                    x1, y1, x2, y2 = coords
                    gcode_file.write(f"G0 X{x1} Y{y1}\n")  # Move to start
                    gcode_file.write(f"G1 X{x2} Y{y2}\n")  # Draw line
                elif len(coords) == 5:  # Arc
                    x0, y0, radius, start_angle, end_angle = coords
                    # Determine direction based on start and end angles
                    if start_angle < end_angle:
                        direction = "G3"  # Counter-clockwise
                    else:
                        direction = "G2"  # Clockwise
                        
                    gcode_file.write(f"G0 X{x0} Y{y0}\n")  # Move to center
                    gcode_file.write(f"{direction} I{radius} J0\n")

            gcode_file.write("M30 ; Program end\n")
            gcode_file.write("%\n")