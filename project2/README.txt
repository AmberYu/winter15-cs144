Ebay Database - Project 2
01/30/2015

Team: FlyingKoala
Member: Guangli Wu, 904363455, wuguangli@ucla.edu
        Jingzhi Yu, 604514516, yujingzhi91@gmail.com



----------------------------
1. Relational Schema Design
----------------------------

Item(ItemID, Seller, Name, Currently, First_Bid, Bid_Num, Buy_Price, Location, Country, Latitude, Longitude, Started, Ends, Description)
PrimaryKey: ItemID

User(UserID, SellerRating, BidRating, Location, Country)
PrimaryKey: UserID

Bid(BidID, Bidder, ItemID, Time, Amount)
PrimaryKey: BidID

ItemCategory(ItemID, Category)
PrimaryKey: ItemID



---------------------------
2. Functional Dependencies
---------------------------

All non-trivial functional dependencies are exactly "primarykey -> the rest attributes" relations in the schemas above.


--------------------------
3. Relations in BCNF
--------------------------

Yes. All functional dependencies contain a key on the left hand side.


--------------------------
4. Relations in 4NF
--------------------------

Yes. 
