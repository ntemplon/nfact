/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tester;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author NathanT
 */
public class BracketHasher {
    public static void main(String[] args) {
        final List<String> unsorted = Arrays.asList(new String[] {
            "Kentucky",
            "Hampton",
            "Cincinnati",
            "Purdue",
            "West Virginia",
            "Buffalo",
            "Maryland",
            "Valparaiso",
            "Butler",
            "Texas",
            "Notre Dame",
            "Northeastern",
            "Wichita State",
            "Indinaa",
            "Kansas",
            "New Mexico State",
            "Wisconsin",
            "Coastal Caro",
            "Oregon",
            "Oklahoma State",
            "Arkansas",
            "Wofford",
            "UNC",
            "Harvard",
            "Xavier",
            "Ole Miss",
            "Baylor",
            "Georgia State",
            "VCU",
            "Ohio State",
            "Arizona",
            "TX SOU",
            "Villanova",
            "Lafayette",
            "NC State",
            "LSU",
            "UNI",
            "Wyoming",
            "Louisville",
            "UC Irvine",
            "Providence",
            "Boise/Day",
            "Oklahoma",
            "Albany (NY)",
            "Michigan State",
            "Georgia",
            "Virginia",
            "Belmont",
            "Duke",
            "UNF/RMU",
            "San Diego State",
            "Saint John's",
            "Utah",
            "SF Austin",
            "Georgetown",
            "E Wash",
            "SMU",
            "UCLA",
            "Iowa State",
            "UAB",
            "Iowa",
            "Davidson",
            "Gonzaga",
            "ND State"
        });
        
        final Map<String, String> capsToLeg = unsorted.stream()
                .collect(Collectors.toMap(
                        (String name) -> name.trim().replace("\\w", "").toUpperCase(),
                        (String name) -> name
                ));
        
        final List<String> sorted = capsToLeg.keySet().stream()
                .collect(Collectors.toList());
        sorted.sort((String first, String second) -> second.hashCode() - first.hashCode());
        
        sorted.stream()
                .map((String name) -> capsToLeg.get(name))
                .forEach((String name) -> System.out.println(name));
    }
}
