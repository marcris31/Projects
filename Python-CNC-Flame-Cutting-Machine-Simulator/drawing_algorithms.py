import tkinter as tk
from tkinter import *
import numpy as np
import math


class DrawingAlgorithms:
    @staticmethod

    def draw_line(canvas, x1, y1, x2, y2, tag=None):
        if tag:
            canvas.create_line(x1, y1, x2, y2, fill="black", tags=tag)
        else:
            canvas.create_line(x1, y1, x2, y2, fill="black")

    @staticmethod
    def bresenham_line(canvas, x1, y1, x2, y2):
         # Bresenham's Line Algorithm
        dx = x2 - x1
        dy = y2 - y1
        x, y = x1, y1

        if dy < 0:
            dy = -dy
            stepy = -1
        else:
            stepy = 1

        if dx < 0:
            dx = -dx
            stepx = -1
        else:
            stepx = 1

        dy *= 2
        dx *= 2

        if dx > dy:
            fraction = dy - (dx / 2)
            while x != x2:
                canvas.create_line(x, y, x, y, fill="black")
                if fraction >= 0:
                    y += stepy
                    fraction -= dx
                x += stepx
                fraction += dy
        else:
            fraction = dx - (dy / 2)
            while y != y2:
                canvas.create_line(x, y, x, y, fill="black")
                if fraction >= 0:
                    x += stepx
                    fraction -= dy
                y += stepy
                fraction += dx

        canvas.create_line(x2, y2, x2, y2, fill="black")  # Ensure the last point is drawn

    @staticmethod
    def draw_arc(canvas, x0, y0, radius, start_angle, end_angle, tag=None):
        # Ensure angles are in degrees 
        start_angle = start_angle % 360
        end_angle = end_angle % 360
    
         # Calculate the extent
        extent = (end_angle - start_angle) % 360
        if extent == 0 and start_angle != end_angle:
            extent = 360

         # Draw the arc
        if tag:
            canvas.create_arc(x0 - radius, y0 - radius, x0 + radius, y0 + radius,
                          start=start_angle, extent=extent, style=tk.ARC, outline="black", tags=tag)
        else:
            canvas.create_arc(x0 - radius, y0 - radius, x0 + radius, y0 + radius,
                          start=start_angle, extent=extent, style=tk.ARC, outline="black")