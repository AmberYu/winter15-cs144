--1. Find the number of users in the database.
SELECT COUNT(*) FROM Users;

--2. Find the number of items in "New York", (i.e., items whose location is exactly the string "New York"). Pay special attention to case sensitivity. You should match the items in "New York" but not in "new york".
SELECT COUNT(*) 
FROM   Users U, Item I 
WHERE  U.UserID = I.Seller AND U.Location = 'New York';

--3. Find the number of auctions belonging to exactly four categories.
SELECT COUNT(*)
FROM(
SELECT ItemID 
FROM ItemCategroy 
GROUP BY ItemID 
HAVING COUNT(*) = 4) IC;

--4. Find the ID(s) of current (unsold) auction(s) with the highest bid. Remember that the data was captured at the point in time December 20th, 2001, one second after midnight, so you can use this time point to decide which auction(s) are current. Pay special attention to the current auctions without any bid.
SELECT    ItemID
FROM      Item
WHERE     Bid_Num>0 AND Ends > '2001-12-20 00:00:01' AND Currently = (SELECT MAX(I1.Currently)
					   FROM   Item I1
					   WHERE  I1.Bid_Num>0 AND I1.Ends > '2001-12-20 00:00:01');

--5. Find the number of sellers whose rating is higher than 1000.
SELECT COUNT(UserID)
FROM   Users U, Item I
WHERE  U.UserID=I.Seller AND U.Rating > 1000;

--6. Find the number of users who are both sellers and bidders.
SELECT COUNT(UserID)
FROM   Users U, Item I, Bids B
WHERE  U.UserID=I.Seller AND U.UserID=B.Bidder;

--7. Find the number of categories that include at least one item with a bid of more than $100.
SELECT COUNT(*)
FROM   ItemCategroy IC, Bids B
WHERE  IC.ItemID=B.ItemID AND B.Amount > 100;