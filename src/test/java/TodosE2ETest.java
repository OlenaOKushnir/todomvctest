import categories.Smoke;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static pages.TodoMVC.*;
import static pages.TodoMVC.TaskType.ACTIVE;

public class TodosE2ETest extends BaseTest {

    @Test
    @Category(Smoke.class)
    public void testTasksCommonFlow() {

        givenAtAll(ACTIVE, "a");
        toggle("a");
        assertTasksAre("a");

        switchToActive();

        assertNoTasks();
        add("b");
        assertTasksAre("b");

        toggleAll();
        assertNoTasks();

        switchToCompleted();

        assertTasksAre("a", "b");

        // reopen
        toggle("a");
        assertItemsLeft(1);
        assertTasksAre("b");

        clearCompleted();
        assertNoTasks();

        switchToAll();

        assertTasksAre("a");

        delete("a");
        assertNoTasks();
    }
}
