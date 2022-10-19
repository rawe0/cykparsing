#!/bin/bash
#set -x

# File names 
file_names_CNF=(
    "RLR" 
    "LR"
    "RL" 
    "LL" 
    "LRL" 
    "SG");


# Arguments corresponding to a file name
args_CNF=(
    "( ) ) -p f" 
    "()"
    ") ("
    "( )"
    "( ) ( -p b" 
    "aa");

# Create directory strings if it doesnt exist
if [ ! -d strings ]; then
  mkdir -p strings;
fi

# Generate all the strings for TD and BU
for i in $(seq 0 5);
do
    file_name=${file_names_CNF[${i}]};
    arg=${args_CNF[${i}]};
    # Remove file
    rm -rf "strings/${file_name}";
    for j in $(seq 1 60);
    do
        python3 string_generator.py -s $arg -l $((j*50)) >> "strings/${file_name}";
    done
done

# Generate all the strings for naive
for i in $(seq 0 5);
do
    file_name=${file_names_CNF[${i}]};
    arg=${args_CNF[${i}]};
    # Remove file
    rm -rf "strings/${file_name}_N"
    for j in $(seq 1 5);
    do
        python3 string_generator.py -s $arg -l $((j*4)) >> "strings/${file_name}_N";
    done
done

rm -rf "ABAB_CC"
for j in $(seq 1 60);
do
    python3 string_generator_l.py -s ab cc -l $((16*j)) $((8*j)) >> "strings/ABAB_CC";
done

rm -rf "strings/ABAB_B_CC"
for j in $(seq 1 60);
do
    python3 string_generator_l.py -s ab b cc -l $((16*j)) 1 $((8*j)) >> "strings/ABAB_B_CC";
done

rm -rf "strings/AA_B_CC"
for j in $(seq 1 60);
do
    python3 string_generator_l.py -s aa b cc -l $((12*j)) 1 $((12*j)) >> "strings/AA_B_CC";
done