#include <stdio.h>
#include <iostream>
#include <tchar.h>
#include <windows.h>
#include <windowsx.h>
#include <mmsystem.h>
#include <fstream>

#define MAXBITS 256
#define MAXBYTES 32
#define MAXINTS   8

using namespace std;

unsigned char used[MAXBYTES];

//Work forward on used, backward on cantUse.

int lastLevel=1;
int start=0;
int N=9;
int setSize[MAXBITS+1]={0,1,2,2,3,4,4,4,4};  //setSize[k]=size of largest 3-free set on 1...k.
int goal=~1337; //Not leet :'(  Note: this value gets overwritten.
int NChosen=0;

int ones[256]={0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1, 2, 2, 3, 2, 3,
 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3,
 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4,
 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6,
 6, 7, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5,
 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4,
 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5,
 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 3, 4, 4, 5, 4, 5,
 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8};

unsigned char ***usable;
int usableSize[256][256];


unsigned char startingArray[41]={216, 209, 204, 202, 198, 197, 195, 180, 177,
166, 165, 163, 153, 141, 139, 208, 200, 196, 194, 193, 176, 164, 162, 161, 152,
148, 145, 140, 138, 137, 134, 133, 131, 192, 160, 144, 136, 132, 130, 129, 128};
int startingArraySize=41;

FILE* outputfile;

void initialize()
{
    outputfile = fopen("output.txt", "wt");
    usable=new unsigned char**[256];
    for(int i=0;i<256;i++)
        usable[i]=new unsigned char*[256];
    ifstream szin("usableSize.txt");
    int temp;
    for(int i=0;i<256;i++)
        for(int j=0;j<256;j++)
        {
            szin>>temp;
            usable[i][j]=new unsigned char[temp];
            usableSize[i][j]=temp;
        }
    ifstream uin("usable.txt");
    for(int i=0;i<256;i++)
        for(int j=0;j<256;j++)
            for(int k=0;k<usableSize[i][j];k++)
            {
                uin>>temp;
                usable[i][j][k]=temp;
            }
    szin.close();
    uin.close();
}

void reset(unsigned char* cantUse)
{
    lastLevel=((N-1)/8);
    goal=setSize[N-1]+1;
    start=GetTickCount();
    NChosen=0;
    //Zero used, set ones in cantUse.
    for(int i=0;i<MAXBYTES;i++)
    {
        used[i]=0;
        cantUse[i]=0xff;
    }
    //Set zeros in cantUse to fill the scope of the choosable numbers.
    for(int i=0;i<N;i++)
        cantUse[MAXBYTES-1-(i/8)]^=((0x01)<<(i%8));
    if(N&1)
        cantUse[MAXBYTES-1-(((N-1)/2)/8)]^=((0x01)<<(((N-1)/2)%8));
}

void excludeBit(int whichByte,int whichBit,unsigned char* cantUse)
{
    int howFarToShift=MAXBITS-((whichByte*16)+(whichBit*2))-1;
    if(howFarToShift>0)  //Then we shift used to the right!
    {
        int bitShift=howFarToShift&7,byteShift=howFarToShift>>3;
        //First, handle the whole bytes.
        for(int i=0;i<whichByte;i++)
        {
            cantUse[byteShift+i]|=(used[i]>>bitShift);
            cantUse[byteShift+i+1]|=(used[i]<<(8-bitShift));
        }
        //No need to worry about the bits in whichByte, we know it's empty.
        //We're done.
    }
    else    //Shift to the left instead.
    {
        throw "You see, when I say \"bare,\" I mean I haven't even added handling for N>=128.  Yeah...";
    }
}

void copyAToB(unsigned char* ac,unsigned char* bc)
{
    unsigned int* a=(unsigned int*)ac,*b=(unsigned int*)bc;
    for(int i=0;i<MAXINTS;i++)
        b[i]=a[i];
}

void orAToB(unsigned char* ac,unsigned char* bc)
{
    unsigned int* a=(unsigned int*)ac,*b=(unsigned int*)bc;
    for(int i=0;i<MAXINTS;i++)
        b[i]|=a[i];
}
//cantUse[MAXBYTES-1-(i/8)]^=((0x01)<<(i%8))
bool choose(int level,unsigned char* choices,int nchoices,
    unsigned char* cantUse)
{
    if(nchoices==0||(cantUse[MAXBYTES-1-((N-1)/8)]&((0x01)<<((N-1)%8))))
        return false;
    if(level==lastLevel)
        if((ones[choices[0]]+NChosen)==goal)
        {
            used[level]=choices[0];
            return true;
        }
        else
            return false;
    unsigned char toOr[MAXINTS][MAXBYTES];
    unsigned char toPass[MAXBYTES];
    for(int i=0;i<MAXINTS;i++)
    {
        copyAToB(cantUse,toOr[i]);
        excludeBit(level,i,toOr[i]);
    }
    for(int i=0;i<nchoices;i++)
    {
        unsigned char subseq=choices[i];
        /*If we have chosen c and we need to choose (k=goal-c) out of the
         remaining m numbers, quit if a(m)<k.  This should offer a HUGE boost,
          and improves itself dynamically based on previous results.*/
        if(setSize[N-(level+1)*8]<goal-NChosen-ones[subseq])
            return false;
        used[level]=subseq;
        NChosen+=ones[subseq];
        copyAToB(cantUse,toPass);
        for(int j=0;j<MAXINTS;j++)
            if(subseq&(0x80>>j))
                orAToB(toOr[j],toPass);
        if(choose(level+1,usable[subseq][toPass[MAXBYTES-1-(level+1)]],
            usableSize[subseq][toPass[MAXBYTES-1-(level+1)]],toPass))
                return true;
        NChosen-=ones[subseq];
    }
    used[level]=0;
    return false;
}

int main(int argc, char* argv)
{
    initialize();      
    if(!outputfile)
    {
        printf("It don't work, chief.");
        return 0;
    }
    unsigned char cantUse[32];
    for(;N<=MAXBITS;N++)
    {
        //Set everything up for the next test.
        reset(cantUse);
        
        fprintf(outputfile, "Looking for a set of size %d in 1...%d: ",goal,N);
        printf("Looking for a set of size %d in 1...%d: ",goal,N);
        
        bool itWorks=choose(0,startingArray,startingArraySize,cantUse);
        setSize[N]=setSize[N-1];
        if(itWorks)
        {
            setSize[N]++;
            printf("It exists.\n");
            fprintf(outputfile,"It exists.\n");
            for(int i=0;i<MAXBITS;i++)
            {
                if(used[i/8]&(0x80>>(i&7)))
                {
                    printf("%d ",i+1);
                    fprintf(outputfile,"%d ",i+1);
                }
            }
            printf("\n");
            fprintf(outputfile,"\n");
        }
        else
        {
            printf("There isn't one.\n");
            fprintf(outputfile,"There isn't one.\n");
        }
        printf("(%0.2f seconds)\n",
            (float)( GetTickCount() - start ) / 1000.0f);
        fprintf(outputfile, "(%0.2f seconds)\n",
            (float)( GetTickCount() - start ) / 1000.0f );
        fflush(outputfile);
    }
    if(outputfile)
        fclose(outputfile);
    return 0;
}
