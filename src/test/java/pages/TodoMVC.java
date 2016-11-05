package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;

public class TodoMVC {

    public static ElementsCollection tasksCollection = $$("#todo-list>li");
    public static SelenideElement addTasks = $("#new-todo");

    @Step
    public static void add(String... taskTexts) {
        for (String text : taskTexts)
            addTasks.setValue(text).pressEnter();
    }

    @Step
    public static void assertItemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(String.valueOf(count)));
    }

    @Step
    public static void assertTasksAre(String... taskTexts) {
        tasksCollection.filter(visible).shouldHave(exactTexts(taskTexts));
    }

    public static SelenideElement startEditing(String oldTaskText, String newTaskText) {
        tasksCollection.find(exactText(oldTaskText)).doubleClick();
        return tasksCollection.find(cssClass("editing")).find(".edit").setValue(newTaskText);
    }

    @Step
    public static void edit(String oldTaskText, String newTaskText) {
        startEditing(oldTaskText, newTaskText).pressEnter();
    }

    @Step
    public static void editByPressTab(String oldTaskText, String newTaskText) {
        startEditing(oldTaskText, newTaskText).pressTab();
    }

    @Step
    public static void cancelEdit(String oldTaskText, String newTaskText) {
        startEditing(oldTaskText, newTaskText).pressEscape();
    }

    @Step
    public static void cancelEditClickOutside(String oldTaskText, String newTaskText) {
        startEditing(oldTaskText, newTaskText);
        $("#new-todo").click();
    }

    @Step
    public static void delete(String taskText) {
        tasksCollection.find(exactText(taskText)).hover().$(".destroy").click();
    }

    @Step
    public static void toggle(String taskText) {
        tasksCollection.find(exactText(taskText)).$(".toggle").click();
    }

    @Step
    public static void assertTaskCompleted(String taskText) {
        tasksCollection.filter(cssClass("completed")).shouldHave(exactTexts(taskText));
    }

    @Step
    public static void assertNoTasks() {
        tasksCollection.filter(visible).shouldBe(empty);
    }

    @Step
    public static void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    public static void clearCompleted() {
        $("#clear-completed").click();
    }

    @Step
    public static void switchToAll() {
        $(By.linkText("All")).click();
    }

    @Step
    public static void switchToActive() {
        $(By.linkText("Active")).click();
    }

    @Step
    public static void switchToCompleted() {
        $(By.linkText("Completed")).click();
    }

    //HELREPS

    public static enum TaskType {
        ACTIVE("false"),
        COMPLETED("true");

        private String taskTypeString;

        TaskType(String taskTypeString) {
            this.taskTypeString = taskTypeString;
        }

        @Override
        public String toString() {
            return taskTypeString;
        }
    }

    public static void given(Filter filter, Task... tasks) {
        ensureUrlOpened(filter);
        addTasks.shouldBe(enabled);
        List<String> storageText = new ArrayList<String>();
        for (Task task : tasks) {
            storageText.add(String.valueOf(task));
        }
        String storageString = "localStorage.setItem(\"todos-troopjs\", \"[" + String.join(", ", storageText) + "]\")";
        executeJavaScript(storageString);
        executeJavaScript("location.reload()");
        addTasks.shouldBe(enabled);
        tasksCollection.shouldHave(size(tasks.length));
    }

    public enum Filter {
        ALL(""),
        ACTIVE("active"),
        COMPLETED("completed");

        private String subUrl;

        Filter(String subUrl) {
            this.subUrl = subUrl;
        }

        public String getUrl() {
            return "https://todomvc4tasj.herokuapp.com/#/" + subUrl;
        }
    }

    public static void ensureUrlOpened(Filter filter) {
        if (!url().equals(filter.getUrl())) {
            open(filter.getUrl());
        }
    }

    public static void givenAtAll(Task... tasks) {
        given(Filter.ALL, tasks);
    }

    public static void givenAtActive(Task... tasks) {
        given(Filter.ACTIVE, tasks);
    }

    public static void givenAtCompleted(Task... tasks) {
        given(Filter.COMPLETED, tasks);
    }

    public static Task aTask(TaskType taskType, String taskText) {
        Task aTask = new Task(taskType, taskText);
        return aTask;
    }

    public static Task[] aTasks(TaskType taskType, String... taskTexts) {
        Task[] aTasks = new Task[taskTexts.length];
        for (int i = 0; i < aTasks.length; i++) {
            aTasks[i] = new Task(taskType, taskTexts[i]);
        }
        return aTasks;
    }

    public static void givenAtAll(TaskType taskType, String... taskTexts) {
        givenAtAll(aTasks(taskType, taskTexts));
    }

    public static void givenAtActive(TaskType taskType, String... taskTexts) {
        givenAtActive(aTasks(taskType, taskTexts));
    }

    public static void givenAtCompleted(TaskType taskType, String... taskTexts) {
        givenAtCompleted(aTasks(taskType, taskTexts));
    }

    public static class Task {

        public String taskText;
        public TaskType taskType;

        @Override
        public String toString() {
            return ("{\\\"completed\\\":" + taskType + ", \\\"title\\\":\\\"" + taskText + "\\\"}");
        }

        public Task(TaskType taskType, String taskText) {
            this.taskText = taskText;
            this.taskType = taskType;
        }
    }
}