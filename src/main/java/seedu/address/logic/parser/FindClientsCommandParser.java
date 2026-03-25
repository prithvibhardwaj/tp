package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindClientsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new {@code FindClientsCommand} object.
 */
public class FindClientsCommandParser implements Parser<FindClientsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindClientsCommand
     * and returns a FindClientsCommand object for execution.
     *
     * @throws ParseException if the user input is empty or contains non-alphanumeric keywords.
     */
    @Override
    public FindClientsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientsCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        for (String keyword : nameKeywords) {
            if (!keyword.matches("\\p{Alnum}+")) {
                throw new ParseException("Keywords must be alphanumeric.");
            }
        }

        return new FindClientsCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
