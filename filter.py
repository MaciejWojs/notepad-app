#!/usr/bin/env python3
import sys

def process_file(file):
  ignored_keywords = ['import', '@OptIn', 'package']
  with open(file, 'r') as f:
      for line in f:
          if not any(keyword in line for keyword in ignored_keywords):
              print(line, end='')

if __name__ == "__main__":
  for file in sys.argv[1:]:
      process_file(file)