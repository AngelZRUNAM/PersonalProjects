# By Angel Zuñiga
# this script download the content from a public facebook photo album 

import urllib
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

driver = webdriver.Firefox()
#The link must be the first photo of the album
driver.get("")
i = 0
#You can set up the number of downloads
while i < 81:
    element = driver.find_element_by_class_name("spotlight")
    driver.implicitly_wait(1) 
    src = element.get_attribute('src')
    #Here goes the url and the name of the images
    urllib.urlretrieve(src,'D:\img\\' + str(i)+'imagen.jpg')
    driver.implicitly_wait(1)
    element.send_keys(Keys.ARROW_RIGHT)
    i=i+1
