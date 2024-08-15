import {
  createCheckCircle,
  toggleCheckCircle,
} from "../common/utils/circleCheckmark.js";
import {
  addEventListener,
  toggleClass,
  updateElementDisplay,
} from "../common/utils/eventUtility.js";
import { detectMobileView } from "../common/utils/deviceUtils.js";

export class TaskRenderer {
  constructor(eventQueue, userId) {
    this.isMobileView = detectMobileView();
    this.eventQueue = eventQueue;
    this.userId = userId;
  }

  createTableRow(cells) {
    const row = document.createElement("tr");
    cells.forEach((cell) => row.appendChild(cell));
    return row;
  }

  createTableCell(content, className) {
    const cell = document.createElement("td");
    cell.classList.add(className);
    cell.textContent = content;
    return cell;
  }

  createDividerRow() {
    const dividerRow = document.createElement("tr");
    const dividerCell = document.createElement("td");
    dividerCell.colSpan = 4;
    const divider = document.createElement("hr");
    divider.classList.add("task-divider");
    dividerCell.appendChild(divider);
    dividerRow.appendChild(dividerCell);
    return dividerRow;
  }

  createCheckCircleCell(task, taskCard) {
    const checkCircleCell = document.createElement("td");
    const checkCircleWrapper = createCheckCircle();

    if (task.status === "Complete") {
      checkCircleWrapper.classList.add("checked");
    }

    addEventListener(checkCircleWrapper, "click", () => {
      toggleCheckCircle(checkCircleWrapper);

      // Toggle the task's completion status
      task.toggleTaskCompletion();
      console.debug(
        `Toggled task completion: ${task.name}, new status: ${task.status}`
      );

      // Update the UI to reflect the new status
      this.updateTaskCompletionUI(task, taskCard);

      // Add the updated UserTask to the event queue
      const userTask = task.toUserTask(this.userId);
      this.eventQueue.addEvent(userTask.id, userTask);
      console.log("Added task to event queue:", userTask);
    });

    checkCircleCell.appendChild(checkCircleWrapper);
    return checkCircleCell;
  }

  updateTaskCompletionUI(task, taskCard) {
    if (task.status === "Complete") {
      taskCard.classList.add("completed");
      const descElement = taskCard.querySelector(".task-desc");
      const statusElement = taskCard.querySelector(".task-status");
      if (descElement) descElement.classList.add("completed");
      if (statusElement) statusElement.textContent = "Complete";
    } else {
      taskCard.classList.remove("completed");
      const descElement = taskCard.querySelector(".task-desc");
      const statusElement = taskCard.querySelector(".task-status");
      if (descElement) descElement.classList.remove("completed");
      if (statusElement) statusElement.textContent = "Incomplete";
    }
    console.debug(`Updated task UI for: ${task.name}, status: ${task.status}`);
  }

  renderTaskCard(task) {
    if (this.isMobileView) {
      console.debug("Rendering mobile task card...");
      return this.renderMobileTaskCard(task);
    } else {
      console.debug("Rendering desktop task card...");
      return this.renderDesktopTaskCard(task);
    }
  }

  renderDesktopTaskCard(task) {
    const taskCard = document.createElement("div");
    taskCard.classList.add("task-card");

    const table = document.createElement("table");
    table.classList.add("task-table");

    const headerRow = this.createTableRow([
      this.createTableCell(task.name, "task-name-header"),
      this.createTableCell("Schedule", "meta-title"),
      this.createTableCell("Status", "meta-title"),
      this.createTableCell("Complete", "meta-title"),
    ]);

    const dividerRow = this.createDividerRow();

    const dataRow = this.createTableRow([
      this.createTableCell(task.description, "task-desc"),
      this.createTableCell(task.formatPeriodicity(), "task-periodicity"),
      this.createTableCell(task.status, "task-status"),
      this.createCheckCircleCell(task, taskCard),
    ]);

    table.append(headerRow, dividerRow, dataRow);
    taskCard.appendChild(table);
    this.updateTaskCompletionUI(task, taskCard);

    return taskCard;
  }

  renderMobileTaskCard(task) {
    const taskCard = document.createElement("div");
    taskCard.classList.add("task-card");

    const header = document.createElement("div");
    header.classList.add("task-header");

    const expandIcon = document.createElement("div");
    expandIcon.classList.add("expand-icon");
    addEventListener(expandIcon, "click", () => {
      toggleClass(taskCard, "expanded");
      const description = taskCard.querySelector(".task-desc");
      updateElementDisplay(
        description,
        taskCard.classList.contains("expanded") ? "block" : "none"
      );
    });

    const title = document.createElement("div");
    title.classList.add("task-name-header");
    title.textContent = task.name;

    const checkCircle = this.createCheckCircleCell(task, taskCard);
    checkCircle.classList.add("check-circle-mobile");

    header.append(expandIcon, title, checkCircle);
    const description = document.createElement("div");
    description.classList.add("task-desc");
    description.textContent = task.description;

    taskCard.append(header, description);
    this.updateTaskCompletionUI(task, taskCard);

    return taskCard;
  }
}