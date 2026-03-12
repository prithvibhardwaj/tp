package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SetFocusCommand;
import seedu.address.model.person.WorkoutFocus;
import seedu.address.testutil.TypicalIndexes;

public class SetFocusCommandParserTest {

    private final SetFocusCommandParser parser = new SetFocusCommandParser();

    @Test
    public void parse_missingParts_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetFocusCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " c/1",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetFocusCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " f/Chest",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetFocusCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidFocus_throwsParseException() {
        assertParseFailure(parser, " c/1 f/Ch3st", WorkoutFocus.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validArgs_returnsCommand() {
        SetFocusCommand expected = new SetFocusCommand(TypicalIndexes.INDEX_FIRST_PERSON, new WorkoutFocus("Chest"));
        assertParseSuccess(parser, " c/1 f/Chest", expected);
    }
}
