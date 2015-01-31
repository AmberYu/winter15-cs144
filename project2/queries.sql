
SELECT COUNT(*) FROM Users;

SELECT COUNT(*) 
FROM   Item
WHERE  Item.Location = BINARY 'New York';

SELECT COUNT(*)
FROM(
SELECT ItemID,COUNT(*) 
FROM ItemCategory 
GROUP BY ItemID 
HAVING COUNT(*) = 4) IC;


SELECT Bids.ItemID
FROM Bids
INNER JOIN Item
ON Bids.ItemID = Item.ItemID
WHERE Ends > '2001-12-20 00:00:01'
AND Amount =
        (SELECT MAX(Amount) FROM Bids);

SELECT COUNT(*)
FROM   Users
WHERE  UserID IN (SELECT U.UserID
				  FROM   Users U, Item I
				  WHERE  U.UserID=I.Seller AND U.SellerRating > 1000);


SELECT COUNT(*)
FROM   Users
WHERE  UserID IN (SELECT U.UserID
				  FROM   Users U, Item I, Bids B
				  WHERE  U.UserID=I.Seller AND U.UserID=B.Bidder);


SELECT COUNT(DISTINCT Category)
FROM   ItemCategory AS IC, Bids AS B
WHERE  IC.ItemID=B.ItemID AND B.Amount > 100;