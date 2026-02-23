# EqualSplit

## Description

A simple tool to share the expenses between friends during a vacation for example.
This is where the idea came from actually.
I came home from a vacation with friends and we all had different expenses we shared.
How will we split this equally? I could just calculate it, but then again, 
  
  "why spend 5 minutes on a task manually when you can spend 6 hours trying to automate it?"



 
## Instructions

 - No installation required
 - Windows: Dowload the .zip, extract and launch EqualSplitApp.exe
 - Linux: Download the .tar.gz, extract and run ./bin/EqualSplitApp.

Add as many people you need with their expenses, this cannot be negative as they have either spent money or they havent.
When everyone is accounted for to share the expenses, press calculate to get the list of who sends money to whom and how much. 
This is calculated to get the least amount of transactions between people. 

```
Inputs
  1) Add person to group // Name and expeses for that person. 
  2) Edit person         // Change the amount the person has spent
  3) Remove person       // Remove person based on name or index in the list.
  4) View group          // Prints all members of the group with their expenditure.
  5) Calculate           // Prints the transactions to even out expenses as sender, reciver, and amount.
```

Example 
  ```
list of people
  Person name=Bob, moneySpent=500.5
  Person name=Timmy, moneySpent=843.9
  Person name=Randy, moneySpent=219.7
  Person name=Steve, moneySpent=761.7

Result
  Sender: Randy      -> Reciver: Timmy      Amount: 262,45
  Sender: Randy      -> Reciver: Steve      Amount: 99,30
  Sender: Bob        -> Reciver: Steve      Amount: 80,95
  Total Expenses: 2325,80 | Individual Share: 581,45
```
