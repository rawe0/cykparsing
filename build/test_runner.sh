#!/bin/bash
set -x

generate_strings=$1
bottom_up=$2
top_down=$3
naive=$4
error_correction=$5
linear=$6
nr_runs=$7



if [ $generate_strings = "t" ]
then
    /bin/bash generate_strings.sh    
else
    echo "Skipping generating strings"
fi


if [ ! -d out ]; then
  mkdir -p out;
fi

WBP_names=(
    "RLR" 
    "LR" 
    "LL" 
    "LRL");

SG_names=(
    "SG");

EC_names=(
    "RL"
    "LR"
)

linear_names=(
    "AA_B_CC"
    "ABAB_B_CC"
    "ABAB_CC"
)

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
        java -jar -XX:CompileThreshold=1 ../out/artifacts/CNFParser/cykparsing.jar grammars/well_balanced_parenthesis.txt strings/${file} BU $nr_runs #> "out/BU_$file.csv" 
    done

    # Run BU tests on SG
    echo "Running bottom up tests on stupid grammar"
    for file in $SG_names
    do
        echo $file
        java -jar -XX:CompileThreshold=1 ../out/artifacts/CNFParser/cykparsing.jar grammars/stupid.txt strings/${file} BU $nr_runs #> "out/BU_$file.csv"
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
        java -jar -XX:CompileThreshold=1 ../out/artifacts/CNFParser/cykparsing.jar grammars/well_balanced_parenthesis.txt strings/${file} TD $nr_runs #> "out/TD_$file.csv"
    done

    # Run top down tests on SG
    echo "Running top down tests on stupid grammar"
    for file in $SG_names
    do
        echo $file
        java -jar -XX:CompileThreshold=1 ../out/artifacts/CNFParser/cykparsing.jar grammars/stupid.txt strings/${file} TD $nr_runs #> "out/TD_$file.csv" 
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
        java -jar -XX:CompileThreshold=1 ../out/artifacts/CNFParser/cykparsing.jar grammars/well_balanced_parenthesis.txt strings/${file}_N N $nr_runs #> "out/N_$file.csv" 
    done

    # Run naive tests on SG
    echo "Running naive tests on stupid grammar"
    for file in $SG_names
    do
        echo $file
        java -jar -XX:CompileThreshold=1 ../out/artifacts/CNFParser/cykparsing.jar grammars/stupid.txt strings/${file}_N N $nr_runs #> "out/N_$file.csv" 
    done
else
    echo "Skipping naive"
fi

##########################
## Error Correction
##########################
if [ $error_correction = "t" ]
then
    # Run BU_EC tests on WBP grammar
    echo "Running BU_EC tests on WBP grammar"
    for file in ${EC_names[@]}
    do
        echo $file
        java -jar -XX:CompileThreshold=1 ../out/artifacts/CNFParser/cykparsing.jar grammars/well_balanced_parenthesis.txt strings/${file} BU_EC_S $nr_runs #> "out/BU_EC_S_$file.csv"
    done

else
    echo "skipping error correction"
fi

#########################
## Linear
#########################
if [ $linear = "t" ]
then
    # Run naive tests on WBP
    echo "Running linear tests on linear grammar"
    for file in ${linear_names[@]}
    do
        echo $file
        java -jar -XX:CompileThreshold=1 ../out/artifacts/LinearParser/cykparsing.jar grammars/linear_grammar.txt strings/${file} TD_L $nr_runs #> "out/TD_L_$file.csv" 
    done
    for file in ${linear_names[@]}
    do
        echo $file
        java -jar -XX:CompileThreshold=1 ../out/artifacts/LinearParser/cykparsing.jar grammars/linear_grammar.txt strings/${file} TD_C $nr_runs #> "out/TD_C_$file.csv" 
    done
else
    echo "Skipping linear"
fi