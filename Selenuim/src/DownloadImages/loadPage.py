# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

import urllib
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

driver = webdriver.Firefox()
driver.get("https://www.facebook.com/Kai.Sama037/photos/a.1124480057627384.1073741847.1006097639465627/1124480344294022/?type=3&theater")
i = 0
while i < 81:
    element = driver.find_element_by_class_name("spotlight")
    driver.implicitly_wait(1) 
    src = element.get_attribute('src')
    urllib.urlretrieve(src,'D:\img\\' + str(i)+'imagen.jpg')
    driver.implicitly_wait(1)
    element.send_keys(Keys.ARROW_RIGHT)
    i=i+1



