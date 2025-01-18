# Final Project - Industry

Begin by forking this repository into your lab exercises namespace by clicking the ```fork``` button.

![](spec/template/fork-button.png)

Select your lab exercises namespace as the fork destination on the resulting window.

Once forked, clone the repository to your computer by following the instructions for your editor.

This is an individual assignment, and must be completed without assistance from other students, external persons, or online solution providers. You are welcome to use static online resources without other limitations.

Your completed code must be pushed to Gitlab at a time specified on moodle. Submissions delivered after this period will not be considered due to grade submission deadlines.

# Overview

In this project you will develop a Java Swing application for managing product inventory. This will make use of the skills you have learnt through the _Programming for Industry_ course. The project will give you the opportunity to show how you can use online resources to discover and apply content not taught within the course.


## Project Brief

The goal of this project is to create a file-based inventory management and point-of-sale application, analogous to those you might find in online or in-person retail businesses. This application will have 2 major components: The first being the 'backend' inventory management facet, where products for sale can be added, removed, and modified; and the second being the 'frontend' point-of-sale where products can be added to a sale, and a receipt produced.

Data used by this application will be written to, and read from a local file on the computer. The format for this file is not specified, it is up to you to choose an appropriate format for your application.


## Marking Criteria

This project is worth 15% of your final grade for the _Programming for Industry_ course. The project grade is weighted based on the following categories.


| Category              | Grade |
|-----------------------|-------|
| Swing Interface       | 50%   |
| Design Pattern usage  | 10%   |
| Code quality          | 30%   |
| Version control usage | 10%   |


### Swing Interface (50%)

In the **Functionality Requirements** section below, a list of all required functional features are detailed. This grade will be determined by the overall completion of required features, and the functionality of each of these features.


### Design Pattern usage (10%)

Your code should demonstrate appropriate usage of design patterns where applicable.


### Code Quality (30%)

Your code must be easily understandable by third parties, and conform to best practices. This includes the use of appropriate variable and identifier names, sufficient commenting, and breaking your code up into appropriate modules, amongst other considerations. It should be written in a way that would make it easy for other people to understand and modify.

You should use code organisation and quality techniques covered in the course content where possible and appropriate. This would include use of design patterns, refactoring, and testing where possible.


### Version control usage (10%)

When working on large projects, use of version control is very important to ensure that work can be undone and recovered to restore functionality in the event that errors were made in development. Usage of git is expected, and regular committing with relevant commit messages will be evaluated. Grade will be determined through evaluation of the project commit log. It is expected that there will be a single commit per day (averaged over the duration of the project) at an **absolute minimum**.


# Requirements

These requirements are given in 3 parts, detailed below in the **Welcome Screen**, **Inventory Manager**, and **Point of Sale** sections. Each of these sections are intended to be different screens (`JFrame`s) within the application, but you may choose to present the application using more or fewer frames as desired.


## General Notes

Your application will be making use of a file to store the details of the items in the inventory. You will be reading from, and writing to this file on a regular basis, so you should consider early on how you will architect your application to make this process simple. You should also think about how you are going to store the information within the file - no format has been specified, you may choose for yourself.

Your application should be saving the inventory state to the file after every change, without requiring a save button or hotkey be pressed. Consider early on how you can model your data, and what design patterns could be useful to make this process simple.


## Welcome Screen

1. When the application is first started, the user should be presented with a welcome screen window, which should prompt the user to select an existing filestore, or create a new one.
    + Both options should use a file picker control (`JFileChooser`) for selecting files

2. Following selection of a filestore, the window content should be replaced with 3 options - Close filestore, Open Inventory Manager, and Open Point of Sale.
   + Selecting "Close filestore" should close the current filestore and return the window to the initial create or open filestore prompt
   + Selecting "Open Inventory Manager" should show the inventory manager content
   + Selecting "Open Point of Sale" should show the point of sale content


## Inventory Manager

The inventory manager should allow for products to be created, viewed, modified, and removed in the open filestore using appropriate Swing controls. It should ensure that the inventory is saved to the open filestore regularly, without requiring a specific button be pressed. It should also provide an option to return to the Welcome Screen.

1. Inventory items should consist of:
   + A unique 10-character identifier, consisting only of numbers and uppercase letters
   + A product name
   + A product description
   + A numeric price per unit
   + A numeric stock quantity (how many of this product are in stock)

2. When opened, inventory items should be initially loaded from the selected filestore
    + A new filestore will not contain any items, the application should handle this without error

3. Inventory items should be displayed in a table. This table should be scrollable, so that large numbers of entries can be displayed

4. New inventory items should be able to be created. Values provided for each of the item's fields should be validated, ensuring that the price and stock quantity are numeric values, and that the identifier is unique within the filestore and conforms to the required format

5. Existing inventory items should be able to be removed

6. Existing inventory items should be editable. All fields making up an inventory item should editable, and should be validated before allowing the edit.

7. The inventory should be **searchable** on the inventory identifier, name, and description. When searching, the table displaying the items should update to show only the items that match the search
   + A simple method of clearing the search, returning to a table showing all items, should be provided

8. The inventory should be **filterable** on the stock quantity, providing options for in-stock items (`quantity > 0`), out-of-stock items (`quantity = 0`), and all items (the default)

9. The inventory should be **sortable** on all columns, supporting both ascending and descending sorting options

10. An option to close the inventory manager and return to the welcome screen should be available


## Point of Sale

The point of sale window should allow a user to browse through a list of products, and allow these to be added to a cart. Many items can be added to a cart as a single transaction, and when ready the user can "checkout", removing items from the inventory and producing a formatted receipt. It should also provide an option to return to the Welcome Screen.

1. Inventory items should be loaded from the open filestore and presented within the window for the user to select from. Only items that are in stock should be shown

2. Items can be selected from the inventory and added to a cart. When an item is added to the cart, this should reduce the available quantity of that item. If the available quantity reduces to 0, the item should no longer appear for selection in the inventory

3. Items can be removed from the cart, returning them to the inventory. This should increase the available quantity of that item. If the item was hidden from selection prior to removing it from the cart, it should be made available for selection again

4. Items added to the cart should display in the order that they were added, and items should be added one-by-one

5. Sequential entries in the cart that are the same item should be 'combined' to display as a single item with a listed quantity - e.g., products added in sequence 'A', 'A', 'A', 'B', 'C', 'C', 'A', 'A', 'C' should be displayed as 'A (3)', 'B', 'C (2)', 'A (2)', 'C'. Removing a 'combined' item from the cart should only remove one instance - e.g., removing 'A (3)' should result in 'A (2)'

6. A running total cost of all items in the cart should be displayed. This should be presented as a number with 2 decimal places, prefixed with a $ sign.

7. Users should be able to "checkout" when they have a non-empty cart. This should result in a receipt file being written (see steps 8-10), the items being removed from the inventory filestore, and the cart view being emptied. The user should be able to begin adding items to the newly empty cart again

8. Receipt files should be generated when the user checks out, and the user should be presented with a file picker asking where to save the receipt file. Cancelling this file picker should cancel the checkout process, ensuring that no receipt file is written, the cart is not emptied, and the items have not been removed from the filestore

9. Receipt files should consist of a series of entry rows showing the items ordered, and a final total cost. Each item entry row should include the product name, quantity, total cost, and unit cost. The unit cost should only be present if more than one item was ordered. Unlike the cart view, all entries of the same type should be combined into a single row - using the example cart of 'A', 'A', 'A', 'B', 'C', 'C', 'A', 'A', 'C' the output should have 'A (5)', 'B (1)', 'C (3)'.

10. Receipt rows should be formatted in a columnar fashion, with all items neatly lining up between rows. Example receipt output is shown below.
11. 
```
--------------------------------
1  PRODUCT_A               $7.50
4  PRODUCT_J    ($1.00)    $4.00
10 PRODUCT_Z   ($15.59)  $155.90
================================
   TOTAL                 $167.40
--------------------------------
```

11. An option to close the point of sale window and return to the welcome screen should be available


# Deliverables & Project Submission


## Source Code

Your git repository will serve as the submission for your project source code. Ensure that your repository master branch is up-to-date on or before the due date given at the top of this document. Any commits after this deadline will be ignored by the markers.

If any special setup instructions are required, please document these in the provided [`SETUP.md`](./SETUP.md) file.
