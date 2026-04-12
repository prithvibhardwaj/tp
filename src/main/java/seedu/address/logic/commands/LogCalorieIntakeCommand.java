package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CALORIE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;

/**
 * Logs a calorie intake amount for a client identified by the displayed index.
 * The logged calories overwrite the client's existing daily intake total,
 * without overwriting the client's calorie target.
 */
public class LogCalorieIntakeCommand extends Command {

    public static final String COMMAND_WORD = "log-cal";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Logs calorie intake for the client identified by the index"
            + " number used in the displayed client list."
            + " The calories overwrite the client's existing daily intake.\n"
            + "Parameters: "
            + PREFIX_CLIENT + "CLIENT_INDEX "
            + PREFIX_CALORIE + "CALORIES (must be a non-negative integer; use 0 to reset)\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_CLIENT + "1 " + PREFIX_CALORIE + "500";

    public static final String MESSAGE_LOG_CALORIE_SUCCESS = "Set calorie intake for %1$s: %2$d kcal";
    public static final String MESSAGE_NOT_A_CLIENT = "The person at the specified index is not a Client.";

    private final Index targetIndex;
    private final int calories;

    /**
     * Constructs a {@code LogCalorieIntakeCommand}.
     *
     * @param targetIndex The 1-based index of the client in the displayed list.
     * @param calories    The calorie intake total to set (must be a positive integer).
     */
    public LogCalorieIntakeCommand(Index targetIndex, int calories) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.calories = calories;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredClientList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personAtIndex = lastShownList.get(targetIndex.getZeroBased());

        if (!(personAtIndex instanceof Client)) {
            throw new CommandException(MESSAGE_NOT_A_CLIENT);
        }

        Client clientToEdit = (Client) personAtIndex;
        Client updatedClient = clientToEdit.withCalorieIntake(calories);

        model.setPerson(clientToEdit, updatedClient);
        return new CommandResult(String.format(MESSAGE_LOG_CALORIE_SUCCESS,
                updatedClient.getName(), calories));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LogCalorieIntakeCommand)) {
            return false;
        }

        LogCalorieIntakeCommand otherCommand = (LogCalorieIntakeCommand) other;
        return targetIndex.equals(otherCommand.targetIndex)
                && calories == otherCommand.calories;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("calories", calories)
                .toString();
    }
}
