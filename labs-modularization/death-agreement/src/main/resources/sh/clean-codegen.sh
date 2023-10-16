#!/bin/zsh

echo "Cleaning codegen for continental"

cd ~/workspaces/workspace-springframework/labs-modularization/death-agreement/src/main/resources/com/jwick/continental/deathagreement
rm analyser/*
rm builder/*
rm config/*
rm constantes/*
rm controller/*
rm dto/*
rm enums/*
rm exception/*
rm interfaces/*
rm repository/*
rm service/impl/*
rm service/*

echo "codegen files were deleted!"