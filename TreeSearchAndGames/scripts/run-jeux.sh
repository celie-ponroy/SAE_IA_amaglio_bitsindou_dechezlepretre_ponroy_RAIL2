#!/bin/bash

# command
JAR_DIR='..'
JAR='TreeSearchAndGames.jar'
CMD="java -jar ${JAR_DIR}/${JAR}"

# defaults
P1='random'
P2='random'
GAME='tictactoe'
DEPTH='-1'
RUNS='15'
VERBOSE="no"

# Counters
P1_WINS=0
P2_WINS=0
DRAWS=0
ERRORS=0

# Read cli arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -p1|--player1)
            p1="$2" && shift && shift
            [[ "$p1" == 'random' || "$p1" == 'minmax' || "$p1" == 'alphabeta' ]] && P1=${p1}
            ;;
        -p2|--player2)
            p2="$2" && shift && shift
            [[ "$p2" == 'random' || "$p2" == 'minmax' || "$p2" == 'alphabeta' ]] && P2=${p2}
            ;;
        -g|--game)
            g="$2" && shift && shift
            [[ "$g" == 'tictactoe' || "$g" == 'connect4' || "$g" == 'gomoku' || "$g" == 'mnk' ]] && GAME=${g}
            ;;
        -d|--depth)
            DEPTH="$2" && shift && shift
            ;;
        -n|--nb_runs)
            RUNS="$2" && shift && shift
            ;;
        -h|--help)
            HELP=yes && shift
            ;;
        -v|--verbose)
            VERBOSE=yes && shift
            ;;
        *)
            POSITIONAL_ARGS+=("$1") # save positional arg
            shift # past argument
            ;;
        -*|--*)
            echo "Unknown option $1. Try `basename $0` -h"
            exit 2
            ;;
    esac
done

# usage message
if [[ $HELP == "yes" ]]; then
    echo "
Batch run of game code
Usage: `basename $0` [-h|--help] [-g|--game tictactoe|connect4|gomoku|mnk] [-p1|--player1 random|minmax|alphabeta] [-p2|--player2 random|minmax|alphabeta] [-d|--depth int] [-v|verbose]
    -h    Prints this help
    -g    The game to play (default tictactoe)
    -p1   Player 1 (default random)
    -p2   Player 2 (default random)
    -d    Game tree max depth (default no limit)
    -n    Number of runs (default 15)
    -v    Verbose (Default no)
"
    exit 0
fi

# Go go go
ARGS="-p1 ${P1} -p2 ${P2} -game ${GAME} -d ${DEPTH}"
echo "# Running : $CMD $ARGS"
echo "# Use -h for help."
START_TIME=$(date +%s)

for ((i = 0 ; i < $RUNS ; i++ )); do
    if [[ $VERBOSE == "no" ]]; then
        $CMD $ARGS >/dev/null
        STATUS=$?
    else
        $CMD $ARGS
        STATUS=$?
    fi

    if [[ $STATUS -eq 101 ]]; then
        echo -e "${i}\tP1"
        ((P1_WINS++))
    elif [[  $STATUS -eq 102 ]]; then
        echo -e "${i}\tP2"
        ((P2_WINS++))
    elif [[  $STATUS -eq 100 ]]; then
        echo -e "${i}\tNUL"
        ((DRAWS++))
    else
        echo -e "${i}\tERROR"
        ((ERRORS++))
    fi
done
# End timer
END_TIME=$(date +%s)
TOTAL_TIME=$((END_TIME - START_TIME))
echo "Temps total d'exécution : ${TOTAL_TIME} secondes"

# Affichage des résultats totaux
echo "Résultats finaux :"
echo "Joueur 1 (${P1}) a gagné : ${P1_WINS} fois"
echo "Joueur 2 (${P2}) a gagné : ${P2_WINS} fois"
echo "Matchs nuls : ${DRAWS}"
echo "Erreurs : ${ERRORS}"
