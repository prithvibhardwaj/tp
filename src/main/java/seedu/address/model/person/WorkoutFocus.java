package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.AppUtil;

/**
 * Represents a Client's workout focus (primary muscle group focus).
 * Guarantees: immutable; is valid as declared in {@link #isValidWorkoutFocus(String)}.
 */
public class WorkoutFocus {

    public static final String MESSAGE_CONSTRAINTS = "Focus string must only contain letters.";
    private static final String VALIDATION_REGEX = "[A-Za-z]+";

    public final String value;

    /**
     * Constructs a {@code WorkoutFocus}.
     *
     * @param workoutFocus A valid workout focus.
     */
    public WorkoutFocus(String workoutFocus) {
        requireNonNull(workoutFocus);
        AppUtil.checkArgument(isValidWorkoutFocus(workoutFocus), MESSAGE_CONSTRAINTS);
        this.value = workoutFocus;
    }

    /**
     * Returns true if a given string is a valid workout focus.
     */
    public static boolean isValidWorkoutFocus(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof WorkoutFocus
                && value.equals(((WorkoutFocus) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
