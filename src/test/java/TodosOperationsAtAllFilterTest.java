import categories.Buggy;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static pages.TodoMVC.*;
import static pages.TodoMVC.TaskType.*;

public class TodosOperationsAtAllFilterTest extends BaseTest {

    @Test
    public void testAdd() {
        givenAtAll();

        add("a");
        assertTasksAre("a");
        assertItemsLeft(1);
    }

    @Test
    public void testEdit() {
        givenAtAll(ACTIVE, "a", "b");

        edit("a", "a edited");
        assertTasksAre("a edited", "b");
        assertItemsLeft(2);
    }

    @Test
    public void testCompleteAll() {
        givenAtAll(ACTIVE, "a");

        toggleAll();
        assertTasksAre("a");
        assertItemsLeft(1);
    }

    @Test
    public void testClearCompleted() {
        givenAtAll(COMPLETED, "a");

        clearCompleted();
        assertNoTasks();
    }

    @Test
    public void testReopen() {
        givenAtAll(COMPLETED, "a");

        toggle("a");
        assertTasksAre("a");
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAll() {
        givenAtAll(COMPLETED, "a", "b", "c");

        toggleAll();
        assertTasksAre("a", "b", "c");
    }

    @Test
    public void testSwitchFromActiveToAll() {
        givenAtActive(aTask(COMPLETED, "a"),
                aTask(ACTIVE, "b"),
                aTask(ACTIVE, "c"));

        switchToAll();
        assertTasksAre("a", "b", "c");
        assertItemsLeft(2);
    }

    @Test
    public void testCancelEditByPressEsc() {
        givenAtAll(ACTIVE, "a", "b", "c");

        cancelEdit("a", "a edited");
        assertTasksAre("a", "b", "c");

    }

    @Test
    public void testEditByClickOutside() {
        givenAtAll(ACTIVE, "a", "b");

        cancelEditClickOutside("b", "b edited");
        assertTasksAre("a", "b edited");
        assertItemsLeft(2);
    }

    @Test
    @Category(Buggy.class)
    public void testEditByPressTab() {
        givenAtAll(ACTIVE, "a", "b");

        editByPressTab("b", "b edited");
        assertTasksAre("a", "b edited");
        assertItemsLeft(2);
    }
}
