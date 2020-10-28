/*
UserIO interface
will be used for the menu system
 */
package com.sg.flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Handles I/O, taking prompts as arguments with overloads to take within a
 * range
 *
 * @author Narish Singh
 */
public interface UserIO {

    /**
     * Print a message
     *
     * @param message {String} message to be printed to console
     */
    void print(String message);

    /**
     * Print a prompt and read line
     *
     * @param prompt {String} prompt to be printed to console
     * @return {String} user's String
     */
    String readString(String prompt);

    /**
     * Print a prompt and read in an integer
     *
     * @param prompt {String} prompt to be printed to console
     * @return {int} user's integer
     */
    int readInt(String prompt);

    /**
     * Print a prompt and read in an integer, accepting only if it is within
     * specified range
     *
     * @param prompt {String} prompt to be printed to console
     * @param min    {int} minimum integer for acceptable inputs
     * @param max    {int} maximum integer for acceptable inputs
     * @return {int} user's integer in range
     */
    int readInt(String prompt, int min, int max);

    /**
     * Print a prompt and read in a double
     *
     * @param prompt {String} prompt to be printed to console
     * @return {double} user's double
     */
    double readDouble(String prompt);

    /**
     * Print a prompt and read in a double, accepting only if it is within
     * specified range
     *
     * @param prompt {String} prompt to be printed to console
     * @param min    {double} minimum double for acceptable inputs
     * @param max    {double} maximum double for acceptable inputs
     * @return {double} user's double in range
     */
    double readDouble(String prompt, double min, double max);

    /**
     * Print a prompt and read in a float
     *
     * @param prompt {String} prompt to be printed to console
     * @return {float} user's float
     */
    float readFloat(String prompt);

    /**
     * Print a prompt and read in a float, accepting only if it is within
     * specified range
     *
     * @param prompt {String} prompt to be printed to console
     * @param min    {float} minimum float for acceptable inputs
     * @param max    {float} maximum float for acceptable inputs
     * @return {float} user's float in range
     */
    float readFloat(String prompt, float min, float max);

    /**
     * Print a prompt and read in a long
     *
     * @param prompt {String} prompt to be printed to console
     * @return {long} user's long
     */
    long readLong(String prompt);

    /**
     * Print a prompt and read in a long, accepting only if it is within
     * specified range
     *
     * @param prompt {String} prompt to be printed to console
     * @param min    {long} minimum long for acceptable inputs
     * @param max    {long} maximum long for acceptable inputs
     * @return {long} user's long in range
     */
    long readLong(String prompt, long min, long max);

    /**
     * Get a BigDecimal value from user input
     *
     * @param prompt {String} the prompt to print to the user
     * @return {BigDecimal} the user's BigDecimal value
     */
    BigDecimal readBigDecimal(String prompt);

    /**
     * Get a BigDecimal value from user input between a specified range
     *
     * @param prompt {String} the prompt to print to the user
     * @param min    {BigDecimal} the minimum input
     * @param max    {BigDecimal} the maximum input
     * @return {BigDecimal} the user's BigDecimal value in range
     */
    BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max);

    /**
     * Get a LocalDate value from the user
     *
     * @param prompt {String} prompt to print to the user
     * @return {LocalDate} the user's inputted date as a LocalDate obj
     */
    LocalDate readLocalDate(String prompt);

    /**
     * Get a LocalDate value from the user between a set period of time
     *
     * @param prompt   {String} prompt to print to the user
     * @param earliest {LocalDate} the earliest date the user may enter
     * @param latest   {LocalDate} the latest date the user may enter
     * @return the user's inputted date in range, as a LocalDate obj
     */
    LocalDate readLocalDate(String prompt, LocalDate earliest, LocalDate latest);
}
