Java implementation of micrograd created by Andrej Karpathy from his YouTube lecture: "The spelled-out intro to neural
networks and backpropagation: building micrograd"

* [Origional Video](https://www.youtube.com/watch?v=VMj-3S1tku0)
* The cooresponding GitHub Python Jupiter Notebooks
    - [Frist Half](https://github.com/karpathy/nn-zero-to-hero/blob/master/lectures/micrograd/micrograd_lecture_first_half_roughly.ipynb)
    - [Second Half](https://github.com/karpathy/nn-zero-to-hero/blob/master/lectures/micrograd/micrograd_lecture_second_half_roughly.ipynb)

Note that there are commented out sections of code in the Value class "demo(*) functions" that follow along to his video
to test the math. Graph that opens showing the loss over time when running the demo.

![Graph that opens showing the loss over time](https://raw.githubusercontent.com/caseyuhrig/micrograd_java/master/contrib/lossvstime.png)

The code could use a little cleanup, but a good starting point to build simple neural networks in Java. Java needs
overloaded operators! +1 :grin:

![Graph that opens network structure](https://raw.githubusercontent.com/caseyuhrig/micrograd_java/master/contrib/micrograd-graphviz-java.png)
