package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class MenuOptionTest {
    private static final String FIND_PERSON_INPUT = "1";
    private static final String PRINT_ALL_PEOPLE_INPUT = "2";
    private static final String EXIT_INPUT = "0";
    private static final String PADDED_FIND_PERSON_INPUT = "  1  ";
    private static final String UNLISTED_INPUT = "3";
    private static final String TEXT_INPUT = "find";
    private static final String FIND_PERSON_MENU_LINE = "1. Find a person";
    private static final String EXIT_MENU_LINE = "0. Exit";

    @Test
    void forInput_one_returnsFindPerson() {
        assertEquals(Optional.of(MenuOption.FIND_PERSON), MenuOption.forInput(FIND_PERSON_INPUT));
    }

    @Test
    void forInput_two_returnsPrintAllPeople() {
        assertEquals(Optional.of(MenuOption.PRINT_ALL_PEOPLE),
                MenuOption.forInput(PRINT_ALL_PEOPLE_INPUT));
    }

    @Test
    void forInput_zero_returnsExit() {
        assertEquals(Optional.of(MenuOption.EXIT), MenuOption.forInput(EXIT_INPUT));
    }

    @Test
    void forInput_optionSurroundedBySpaces_returnsThatOption() {
        assertEquals(Optional.of(MenuOption.FIND_PERSON),
                MenuOption.forInput(PADDED_FIND_PERSON_INPUT));
    }

    @Test
    void forInput_numberThatIsNotOnTheMenu_returnsNothing() {
        assertTrue(MenuOption.forInput(UNLISTED_INPUT).isEmpty());
    }

    @Test
    void forInput_text_returnsNothing() {
        assertTrue(MenuOption.forInput(TEXT_INPUT).isEmpty());
    }

    @Test
    void forInput_emptyInput_returnsNothing() {
        assertTrue(MenuOption.forInput("").isEmpty());
    }

    @Test
    void menuLine_findPerson_showsItsNumberAndLabel() {
        assertEquals(FIND_PERSON_MENU_LINE, MenuOption.FIND_PERSON.menuLine());
    }

    @Test
    void menuLine_exit_showsItsNumberAndLabel() {
        assertEquals(EXIT_MENU_LINE, MenuOption.EXIT.menuLine());
    }
}
