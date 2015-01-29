
SELECT COUNT(*) FROM Users;

SELECT COUNT(*) 
FROM   Users U, Item I 
WHERE  U.UserID = I.Seller AND U.Location = 'New York';

SELECT COUNT(*)
FROM(
SELECT ItemID 
FROM ItemCategroy 
GROUP BY ItemID 
HAVING COUNT(*) = 4) IC;


SELECT    ItemID
FROM      Item
WHERE     Bid_Num>0 AND Ends > '2001-12-20 00:00:01' AND Currently = (SELECT MAX(I1.Currently)
					   FROM   Item I1
					   WHERE  I1.Bid_Num>0 AND I1.Ends > '2001-12-20 00:00:01');


SELECT COUNT(*)
FROM   Users
WHERE  UserID IN (SELECT U.UserID
				  FROM   Users U, Item I
				  WHERE  U.UserID=I.Seller AND U.Rating > 1000);


SELECT COUNT(*)
FROM   Users
WHERE  UserID IN (SELECT U.UserID
				  FROM   Users U, Item I, Bids B
				  WHERE  U.UserID=I.Seller AND U.UserID=B.Bidder);


SELECT COUNT(DISTINCT Category)
FROM   ItemCategory AS IC, Bids AS B
WHERE  IC.ItemID=B.ItemID AND B.Amount > 100;