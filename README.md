API TEST FRAMEWORK-> HTTPCLIENT
These microservices provide RESTful API for each part of the system. We need a series of automated tests to protect our APIs against bugs.

The API does these tasks:
Place Order
Fetch Order Details
Driver to Take the Order
Driver to Complete the Order
Cancel Order

An order has these statuses in sequence: ASSIGNING => ONGOING => COMPLETED or CANCELLED
ASSIGNING: looking for a driver to be assigned
ONGOING: a driver has been assigned and working on the order
COMPLETED: the driver has completed the order
CANCELLED: the order was cancelled
