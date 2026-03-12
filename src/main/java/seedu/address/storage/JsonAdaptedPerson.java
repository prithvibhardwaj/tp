package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    public static final String INVALID_TYPE_MESSAGE_FORMAT = "Person's type is invalid!";

    private final String type; // "trainer" or "client"
    private final String name;
    private final String phone;
    private final String email;
    private final String trainerPhone;
    private final String trainerName;
    private final int calorieTarget;
    private final int calorieIntake;
    private final String workoutFocus;
    private final String remark;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("type") String type, @JsonProperty("name") String name,
            @JsonProperty("phone") String phone, @JsonProperty("email") String email,
            @JsonProperty("trainerPhone") String trainerPhone, @JsonProperty("trainerName") String trainerName,
            @JsonProperty("calorieTarget") int calorieTarget,
            @JsonProperty("calorieIntake") int calorieIntake,
            @JsonProperty("workoutFocus") String workoutFocus,
            @JsonProperty("remark") String remark,
            @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.trainerPhone = trainerPhone;
        this.trainerName = trainerName;
        this.calorieTarget = calorieTarget;
        this.calorieIntake = calorieIntake;
        this.workoutFocus = workoutFocus;
        this.remark = remark;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        if (source instanceof Trainer) {
            type = "trainer";
            email = ((Trainer) source).getEmail().value;
            trainerPhone = null;
            trainerName = null;
            calorieTarget = 0;
            calorieIntake = 0;
            workoutFocus = null;
            remark = null;
        } else if (source instanceof Client) {
            Client client = (Client) source;
            type = "client";
            email = null;
            trainerPhone = client.getTrainerPhone().value;
            trainerName = client.getTrainerName().fullName;
            calorieTarget = client.getCalorieTarget();
            calorieIntake = client.getCalorieIntake();
            workoutFocus = client.getWorkoutFocus().map(focus -> focus.value).orElse(null);
            remark = client.getRemark().map(r -> r.value).orElse(null);
        } else {
            type = "unknown";
            email = null;
            trainerPhone = null;
            trainerName = null;
            calorieTarget = 0;
            calorieIntake = 0;
            workoutFocus = null;
            remark = null;
        }

        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        if ("trainer".equals(type) || type == null && email != null) { // fallback for existing records without type
            if (email == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                        Email.class.getSimpleName()));
            }
            if (!Email.isValidEmail(email)) {
                throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
            }
            final Email modelEmail = new Email(email);
            return new Trainer(modelName, modelPhone, modelEmail, modelTags);
        } else if ("client".equals(type) || type == null && trainerPhone != null) {
            if (trainerPhone == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "trainerPhone"));
            }
            if (!Phone.isValidPhone(trainerPhone)) {
                throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
            }
            final Phone modelTrainerPhone = new Phone(trainerPhone);

            if (trainerName == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "trainerName"));
            }
            if (!Name.isValidName(trainerName)) {
                throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
            }
            final Name modelTrainerName = new Name(trainerName);

            java.util.Optional<seedu.address.model.person.WorkoutFocus> modelFocus = java.util.Optional.empty();
            if (workoutFocus != null) {
                if (!seedu.address.model.person.WorkoutFocus.isValidWorkoutFocus(workoutFocus)) {
                    throw new IllegalValueException(seedu.address.model.person.WorkoutFocus.MESSAGE_CONSTRAINTS);
                }
                modelFocus = java.util.Optional.of(new seedu.address.model.person.WorkoutFocus(workoutFocus));
            }

            java.util.Optional<seedu.address.model.person.Remark> modelRemark = java.util.Optional.empty();
            if (remark != null) {
                if (!seedu.address.model.person.Remark.isValidRemark(remark)) {
                    throw new IllegalValueException(seedu.address.model.person.Remark.MESSAGE_CONSTRAINTS);
                }
                modelRemark = java.util.Optional.of(new seedu.address.model.person.Remark(remark));
            }

            return new Client(modelName, modelPhone, modelTrainerPhone, modelTrainerName, modelTags,
                    calorieTarget, calorieIntake, modelFocus, modelRemark);
        } else {
            throw new IllegalValueException(INVALID_TYPE_MESSAGE_FORMAT);
        }
    }

}

