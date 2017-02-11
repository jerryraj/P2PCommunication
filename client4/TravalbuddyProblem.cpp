//Travel buddy

#include <iostream>
#include <vector>
#include <set>
#include <cstring>
#include <map>
using namespace std;



  
void wishList(string param1, vector<string> param2)
{
    set<string> cityPerson;    
        char* paramStr = new char[param1.length()+1];
        memcpy(paramStr,param1.c_str(),param1.length()+1);
        char* str = strtok(paramStr, ",");
        
        int psize = 0;
        while(str!=NULL)
              {
                str = strtok(NULL, ",");
                cityPerson.insert((string) str);
          psize++;
              }
  
      multimap<int,string> vectorParams;
  
      for(auto itr = param2.begin(); itr != param2.end(); itr++)
      {
        char*  par= new char[(*itr).length()+1];
        memcpy(par,(*itr).c_str(),(*itr).length()+1);
           
        str = strtok(par, ",");
        string p1(str);
        //set<string> city; 
        int count=0;
        while(str!=NULL)
              {
                str = strtok(NULL, ",");
                //city.add(str);
                if(cityPerson.count((string)str))
                  count++;
              }
        if(count>= psize/2)
        vectorParams.insert(pair<int,string>(count, p1));
      }
  
    for(auto it = vectorParams.begin(); it !=vectorParams.end(); it++)
    {
          cout<<it->second<<endl;
    }
      return;        
      
          



}

// To execute C++, please define "int main()"
int main() {
  
  
  
  wishList("Me,Amsterdam,Barcelona,London,Prague",
           {"U1,Amsterdam,Barcelona,London,Prague",
"U2,Shanghai,Hong Kong,Moscow,Sydney,Melbourne",
"U3,London,Boston,Amsterdam,Madrid",
"U4,Barcelona,Prague,London,Sydney,Moscow"});
  return 0;
}

/*
<user name>,<city 1>,<city 2>...
<user name>,<city 1>,<city 2>...

User A is a travel buddy of user B if A has 50% or more of the cities in his/her wishlist in common with B.

A,City1,2,3,4
B,City1,2,10,11,12,13

1 + 2
B is a buddy of A (2/4)
A is NOT a buddy of B (2/6)

Param 1: string representing your wish list
Param 2: list of strings representing potential buddies' wish lists

e.g.
Me,Amsterdam,Barcelona,London,Prague

U1,Amsterdam,Barcelona,London,Prague
U2,Shanghai,Hong Kong,Moscow,Sydney,Melbourne
U3,London,Boston,Amsterdam,Madrid
U4,Barcelona,Prague,London,Sydney,Moscow

U1, U4, U3

*/