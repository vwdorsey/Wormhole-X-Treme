/*
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme/>
 *
 * Copyright (C) 2011 Ben Echols
 *                    Dean Bailey
 *
 * This file is a modified version from the Bukkit Plugin PermissionsEx v1.20
 * Copyright (C) 2011 t3hk0d3 http://www.tehkode.ru
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.luricos.bukkit.WormholeXTreme.Wormhole.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lycano
 */
public class CommandSyntax {

    protected String originalSyntax;
    protected String regexp;
    protected List<String> arguments = new LinkedList<String>();

    public CommandSyntax(String syntax) {
        this.originalSyntax = syntax;

        this.regexp = this.prepareSyntaxRegexp(syntax);
    }

    public String getRegexp() {
        return regexp;
    }

    private String prepareSyntaxRegexp(String syntax) {
        String expression = syntax;

        Matcher argMatcher = Pattern.compile("(?:[\\s]+)?((\\<|\\[)([^\\>\\]]+)(?:\\>|\\]))").matcher(expression);
        //Matcher argMatcher = Pattern.compile("(\\<|\\[)([^\\>\\]]+)(?:\\>|\\])").matcher(expression);

        int index = 0;
        while (argMatcher.find()) {
            if (argMatcher.group(2).equals("[")) {
                expression = expression.replace(argMatcher.group(0), "(?:(?:[\\s]+)(\"[^\"]+\"|[^\\s]+))?");
            } else {
                expression = expression.replace(argMatcher.group(1), "(\"[^\"]+\"|[\\S]+)");
            }

            arguments.add(index++, argMatcher.group(3));
        }

        return expression;
    }

    public boolean isMatch(String str) {
        return str.matches(this.regexp);
    }

    public Map<String, String> getMatchedArguments(String str) {
        Map<String, String> matchedArguments = new HashMap<String, String>(this.arguments.size());

        if (this.arguments.size() > 0) {
            Matcher argMatcher = Pattern.compile(this.regexp).matcher(str);

            if (argMatcher.find()) {
                for (int index = 1; index <= argMatcher.groupCount(); index++) {
                    String argumentValue = argMatcher.group(index);
                    if (argumentValue == null || argumentValue.isEmpty()) {
                        continue;
                    }

                    if (argumentValue.startsWith("\"") && argumentValue.endsWith("\"")) { // Trim boundary colons
                        argumentValue = argumentValue.substring(1, argumentValue.length() - 1);
                    }

                    matchedArguments.put(this.arguments.get(index - 1), argumentValue);
                }
            }
        }
        return matchedArguments;
    }
}
