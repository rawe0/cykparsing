#!/bin/bash
#set -x

generate=$1
bottom_up=$2
top_down=$3
naive=$4
nr_runs=$5


if [ $1 = "t" ]
then
    # File names 
    file_names=(
        "RLR" 
        "LR" 
        "LL" 
        "LRL" 
        "SG");

    # Arguments corresponding to a file name
    args=(
        ") ( ) -p f" 
        "()"
        "( )"
        "( ) ( -p b" 
        "aa");


    # Generate all the strings for TD and BU
    for i in $(seq 0 4);
    do
        file_name=${file_names[${i}]};
        arg=${args[${i}]};
        # Remove file
        rm -rf $file_name;
        rm -rf "${file_name}_naive"
        for j in $(seq 1 60);
        do
            python3 string_generator.py -s $arg -l $((j)) >> "${file_name}";
            python3 string_generator.py -s $arg -l $((j)) >> "${file_name}_naive";
        done
    done
else
    echo "Skipping generating strings"
fi


WBP_names=(
    "RLR" 
    "LR" 
    "LL" 
    "LRL");

SG_names=(
    "SG");

#########################
## Bottom up
#########################

if [ $bottom_up = "t" ]
then
    # Run BU tests on WBP
    echo "Running bottom up tests on WBP grammar"

    for file in ${WBP_names[@]}
    do
        echo $file
        java -jar -XX:CompileThreshold=1 cykparsing.jar well_balanced_parenthesis.txt $file BU $nr_runs > "BU_$file.csv" 
    done

    # Run BU tests on SG
    echo "Running bottom up tests on stupid grammar"
    for file in $SG_names
    do
        echo $file
        java -jar -XX:CompileThreshold=1 cykparsing.jar stupid.txt $file BU $nr_runs > "BU_$file.csv"
    done
else
    echo "Skipping bottom up"
fi


#########################
## Top down
#########################
if [ $top_down = "t" ]
then
    # Run top down tests on WBP
    echo "Running top down tests on WBPgrammar"
    for file in ${WBP_names[@]}
    do
        echo $file
        java -jar -XX:CompileThreshold=1 cykparsing.jar well_balanced_parenthesis.txt $file TD $nr_runs > "TD_$file.csv"
    done

    # Run top down tests on SG
    echo "Running top down tests on stupid grammar"
    for file in $SG_names
    do
        echo $file
        java -jar -XX:CompileThreshold=1 cykparsing.jar stupid.txt $file TD $nr_runs > "TD_$file.csv" 
    done
else
    echo "Skipping bottom up"
fi


#########################
## Naive
#########################
if [ $naive = "t" ]
then
    # Run naive tests on WBP
    echo "Running naive tests on WBP grammar"
    for file in ${WBP_names[@]}
    do
        echo $file
        java -jar -XX:CompileThreshold=1 cykparsing.jar well_balanced_parenthesis.txt ${file}_naive N $nr_runs > "naive_$file.csv" 
    done

    # Run naive tests on SG
    echo "Running naive tests on stupid grammar"
    for file in $SG_names
    do
        echo $file
        java -jar -XX:CompileThreshold=1 cykparsing.jar stupid.txt ${file}_naive N $nr_runs > "naive_$file.csv" 
    done
else
    echo "Skipping naive"
fi