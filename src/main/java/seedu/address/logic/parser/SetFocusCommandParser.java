package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOCUS;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SetFocusCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.WorkoutFocus;

/**
 * Parses input arguments and creates a new {@code SetFocusCommand} object.
 */
public class SetFocusCommandParser implements Parser<SetFocusCommand> {

    @Override
    public SetFocusCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLIENT, PREFIX_FOCUS);

        if (!arePrefixesPresent(argMultimap, PREFIX_CLIENT, PREFIX_FOCUS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetFocusCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLIENT, PREFIX_FOCUS);

        Index clientIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLIENT).get());
        WorkoutFocus workoutFocus = ParserUtil.parseWorkoutFocus(argMultimap.getValue(PREFIX_FOCUS).get());

        return new SetFocusCommand(clientIndex, workoutFocus);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
