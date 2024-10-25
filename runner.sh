#!/bin/bash

################################################################################
##                       Cloud Computing Course                               ##
##            Runner Script for Heterogenous Storage on the Cloud             ##
##                                                                            ##
##            Copyright 2021-2022: Cloud Computing Course                     ##
##                     Carnegie Mellon University                             ##
##   Unauthorized distribution of copyrighted material, including             ##
##  unauthorized peer-to-peer file sharing, may subject the students          ##
##  to the fullest extent of Carnegie Mellon University copyright policies.   ##
################################################################################

################################################################################
##                      README Before You Start                               ##
################################################################################
# Fill in the functions below for each question.
# You may use any programming language(s) in any question.
# You may use other files or scripts in these functions as long as they are in
# the submission folder.
# All files MUST include source code (e.g. do not just submit jar or pyc files).
#
# We will suggest tools or libraries in each question to enrich your learning.
# You are allowed to solve questions without the recommended tools or libraries.
#
# The colon `:` is a POSIX built-in basically equivalent to the `true` command,
# REPLACE it with your own command in each function.
# Before you fill your solution,
# DO NOT remove the colon or the function will break because the bash functions
# may not be empty!


################################################################################
##                                   Setup                                    ##
################################################################################

setup() {
  # Fill in this helper function to do any setup if you need to.
  #
  # This function will be executed once at the beginning of the grading process.
  # Other functions might be executed repeatedly in arbitrary order.
  # Make use of this function to reduce unnecessary overhead and to make your
  # code behave consistently.
  #
  # e.g. You should compile Java code in this function.
  #
  # However, DO NOT use this function to solve any question or
  # generate any intermediate output.
  #
  # Examples:
  # javac *.java
  # mvn clean package
  # pip3 install -r requirements.txt
  #
  # Standard output format:
  # No standard output needed
  mvn clean package -Dmaven.test.skip=true
}

################################################################################
##                           SQL and Pandas                                   ##
################################################################################
q1() {
  python3 q1.py
}


################################################################################
##                    DO NOT MODIFY ANYTHING BELOW                            ##
################################################################################
RED_FONT='\033[0;31m'
NO_COLOR='\033[0m'
declare -ar mysql=( "q2" "q3" "q4" "q5" "q6" "q7" "q8")
declare -ar mongodb=( "q9" "q10" "q11" "q12")
declare questions=("q1" "${mysql[@]}" "${mongodb[@]}")

last=${questions[$((${#questions[*]}-1))]}
readonly last
readonly usage="This program is used to execute your solution.
Usage:
./runner.sh to run all the questions
./runner.sh -r <question_id> to run one single question
./runner.sh -s to run setup() function
./runner.sh -t <task_name> to run all questions in a task
Example:
./runner.sh -r q1 to run q1
./runner.sh -t mysql/mongodb/redis"

contains() {
  local e
  for e in "${@:2}"; do
    [[ "$e" == "$1" ]] && return 0;
  done
  return 1
}

run() {
  if contains "$1" "${mysql[@]}"; then
    echo -n "$(java -cp target/database_tasks.jar edu.cmu.cs.cloud.MySQLTasks "$1")"
  elif contains "$1" "${mongodb[@]}"; then
    echo -n "$(java -cp target/database_tasks.jar edu.cmu.cs.cloud.MongoDBTasks "$1")"
  else
    echo -n "$("$1")"
  fi
}

while getopts ":hsr:t:" opt; do
  case $opt in
    h)
      echo "$usage" >&2
      exit
      ;;
    s)
      setup
      echo "setup() function executed" >&2
      exit
      ;;
    r)
      question=$OPTARG
      if contains "$question" "${questions[@]}"; then
        run "$question"
      else
        echo "Invalid question id" >&2
        echo "$usage" >&2
        exit 2
      fi
      exit
      ;;
    t)
      task=$OPTARG
      if [ "$task" == "mysql" ]; then
        # mysql task"
        run "q1"
        echo ""
        for question in "${mysql[@]}"; do
          run "$question"
          echo ""
        done
      elif [ "$task" == "mongodb" ]; then        # mongodb task
        for question in "${mongodb[@]}"; do
          run "$question"
          echo ""
        done
      elif [ "$task" == "redis" ]; then
        mvn test >&2 && mvn jacoco:report >&2
      else
        echo "Invalid task: $task" >&2
      fi
      exit
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      echo "$usage" >&2
      exit 2
      ;;
  esac
done

if [ -z "$1" ]; then
  setup 1>&2
  echo "setup() function executed" >&2
  echo -e "The ${RED_FONT}JSON escaped${NO_COLOR} answers generated by executing your solution are: " >&2
  echo "{"
  for question in "${questions[@]}"; do
    echo -n ' '\""$question"\":"$(run "$question" | python3 -c 'import json, sys; print(json.dumps(sys.stdin.read()))')"
    if [[ "${question}" == "$last" ]]; then
      echo ""
    else
      echo ","
    fi
  done
  echo "}"
else
  echo "Invalid usage" >&2
  echo "$usage" >&2
  exit 2
fi
