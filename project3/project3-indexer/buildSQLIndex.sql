CREATE TABLE Location
(
	ItemID VARCHAR(10) NOT NULL,
	Coordinates POINT NOT NULL,
	PRIMARY KEY (ItemID)
) ENGINE = MyISAM;


-- populate the table, x = lat, y = lon
INSERT INTO Location (ItemID, Location)
	SELECT ItemID, POINT(Latitude, Longitude)
	FROM Item
	WHERE Latitude != "" AND Longitude != "";


CREATE SPATIAL INDEX Coordinates_Index ON Location(Coordinates);