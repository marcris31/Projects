import threading
import time
import tkinter as tk
from drawing_algorithms import DrawingAlgorithms
from tkinter import *


class Simulation:
    def __init__(self, interface):
        self.interface = interface
        self.is_running = False
        self.simulation_thread = None

    def simulate_cutting(self,  x, y, x1, y1): #, arc_radius
        # Retrieve values safely, consider adding validation
        try:
            while self.is_running:
                x = int(self.interface.x_text.get())
                y = int(self.interface.y_text.get())
                x1 = int(self.interface.x1_text.get())
                y1 = int(self.interface.y1_text.get())
                #arc_radius = int(self.interface.arc_radius_text.get())
        except ValueError:
            print("Please enter valid numeric coordinates.")
            self.stop()
            return

        while self.is_running:
            # Simulate cutting head movement
            self.interface.canvas.delete("cutting_line", "cutting_arc")
            
            # Draw new cutting path
            DrawingAlgorithms.draw_line(self.interface.canvas, x, y, x1, y1, tag="cutting_line")

            #DrawingAlgorithms.draw_arc(self.interface.canvas, x1, y1, arc_radius, 0, 180, tag="cutting_arc")
            

            self.interface.canvas.after(100, self.interface.canvas.update)
            time.sleep(0.1)

    def start(self):
        if not self.is_running:
            self.is_running = True
            self.interface.label.config(text="Machine is running...")
            self.interface.start_button.config(state=tk.DISABLED)
            self.interface.stop_button.config(state=tk.NORMAL)

 # Retrieve and validate coordinates from the interface
            try:
                x = int(self.interface.x_text.get())
                y = int(self.interface.y_text.get())
                x1 = int(self.interface.x1_text.get())
                y1 = int(self.interface.y1_text.get())
                #arc_radius = int(self.interface.arc_radius_text.get())
                # If you have additional fields for arc or other shapes, retrieve them here
            except ValueError:
                # Handle invalid input
                self.interface.label.config(text="Invalid coordinates. Please enter numeric values.")
                self.stop()
                return

            # Start the drawing process in a new thread
            self.simulation_thread = threading.Thread(target=self.simulate_cutting, args=(x, y, x1, y1), daemon=True)
            self.simulation_thread.start()

    def stop(self):
        if self.is_running:
            self.is_running = False
            if self.simulation_thread is not None:
                self.simulation_thread.join()  # Wait for the thread to finish
            self.interface.label.config(text="Machine is stopped.")
            self.interface.start_button.config(state=tk.NORMAL)
            self.interface.stop_button.config(state=tk.DISABLED)