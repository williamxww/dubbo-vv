module Demo {  
    interface Printer  
    {  
        int printString(string s);
    };

    interface DemoService{
        void say(string s);
        int calculate(int a, int b);
    };

};