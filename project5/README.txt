Ebay Database - Project 5
03/13/2015

Team: FlyingKoala
Member: Guangli Wu, 904363455, wuguangli@ucla.edu
        Jingzhi Yu, 604514516, yujingzhi91@gmail.com



Q1: For which communication(s) do you use the SSL encryption? If you are encrypting the communication from (1) to (2) in Figure 2, for example, write (1)°˙(2) in your answer.

(4)->(5), (5)->(6)


Q2: How do you ensure that the item was purchased exactly at the Buy_Price of that particular item?

when the item page is generated and if Buy_price has value, we associate the Buy_price that sunflower provides to a HTTP session. Once the session is established, we don’t have to show the Buy_price information in the URL since Tomcat will be able to retrieve the associated information to process any HTTP request from the same browser.