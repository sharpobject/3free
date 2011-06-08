#include <cstdio>
#include <cstring>
#include <algorithm>
#include <vector>
#include <boost/foreach.hpp>
#include <boost/date_time/posix_time/posix_time_types.hpp>

#define TEMPLATE_HACK
#define min(x,y) (x)+((((y)-(x))>>(31))&((y)-(x)))
#define uchar unsigned char
#define N_BOWLS 117
#define N_ORANGES 31
#define ARR_LEN ((N_BOWLS + 7)/8)
#define INT_ARR_LEN ((ARR_LEN + 3) / 4)
#define PADDED_ARR_LEN (INT_ARR_LEN * 4)
#define HIGH_BIT 0x80

using namespace boost::posix_time;
using namespace std;

// State: Which slots I may no longer place oranges in.
// Each level of the stack gets its own copy.
uchar banned[ARR_LEN+1][PADDED_ARR_LEN];
// State: the oranges I have placed so far.
uchar placed_so_far[PADDED_ARR_LEN];

// The number of 1 bits in idx.
uchar bits[256];
// The bits in idx, reversed.
uchar rev[256];

// next_word_bans[mask] contains the bits that can't be chosen after mask.
// For instance, 00010011 maps to 11010000
uchar next_word_bans[256];

// three_free[mask] is a vector containing all 3-free 8-bit words
// that have an empty intersection with mask.
vector<uchar> three_free[256];
int set_size[256] = {0,1,2,2,3,4,4,4,4,5,5,6,6,7,8,8,8,8,8,8,9,9,9,9,10,
 10,11,11,11,11,12,12,13,13,13,13,14,14,14,14,15,
 16,16,16,16,16,16,16,16,16,16,17,17,17,18,18,18,
 18,19,19,19,19,19,20,20,20,20,20,20,20,20,21,21,
 21,22,22,22,22,22,22,22,22,23,23,
 24,24,24,24,24,24,24,24,
 25,25,25,
 26,26,26,26,26,
 27,27,27,27,
 28,28,28,28,28,28,28,
 29,29,29,
 30,30,30,30,30,30,30,
 31,
 32,32,32,32,32,32,32,32,32,32,32,32,32,32,32};

vector<uchar> first_level_array;

// A vector containing all possible 3APs that fit in 8 bits.
// 3AP = Arithmetic progression of length 3.
vector<uchar> byte_3AP;
// Reflects a about b into c and the following 2 bytes.
void reflect(const uchar a, const uchar b, uchar* const c)
{
    int buf = 0;
    int base = ((int) rev[a]) << 15;
    for(uchar i=0; i<8; ++i)
        if(b & (HIGH_BIT>>i))
            buf |= base >> (2*i);
    c[0] |= (uchar)(buf>>16);
    c[1] |= (uchar)(buf>>8);
    c[2] |= (uchar)buf;
}

inline void cpy(int *a,int *b)
{
    for(int i=0;i<INT_ARR_LEN;i++)
        *(b++)=*(a++);
}

// Calculates the number of three-free sets given the current state,
// placing the remaining n items in slots beginning at idx.
#ifdef TEMPLATE_HACK
template <const int idx>
bool dfs(const int n)
#else
bool dfs(const int idx, const int n)
#endif //TEMPLATE_HACK
{
    if(banned[idx][ARR_LEN-1]&((0x80) >> ((N_BOWLS-1) % 8)))
        return false;
    const int bans = banned[idx][idx];
    // We'll use this later -- see comments around line 95
    //const int n_flips = (ARR_LEN-(((ARR_LEN)>>(31))&(ARR_LEN)))-idx;
    //const int n_flips = idx-(((ARR_LEN-2*idx)>>(31))&(ARR_LEN-2*idx));
    //const int junk = ARR_LEN-idx;
    //const int n_flips = min(idx,junk);
    const int n_flips = idx<ARR_LEN-idx ? idx : ARR_LEN-idx;
    // For each word that is actually available to us...
    BOOST_FOREACH(const uchar word, idx?three_free[bans]:first_level_array)
    {
        // This is a bit involved.  The banning array works by banning bits
        // outright, but what if we really need to ban a certain combination
        // of bits?  For example, if we pick 0000 0001 then we can follow it
        // with either 1000 0000 or 0100 0000 but not with 1100 0000.

        // Rather than make a bigger table to look this stuff up, we'll just
        // check whether we're doing something like that.
        if(idx != 0 && ((next_word_bans[rev[word]] &
                rev[placed_so_far[idx-1]])!=0))
            continue;

        /*If we have chosen c and we need to choose (k=N_ORANGES-c) out of the
         remaining m numbers, quit if a(m)<k.  This should offer a HUGE boost,
         and improves itself dynamically based on previous results.*/

        const int bitcnt = bits[word];

        if(set_size[N_BOWLS-((idx+1)<<3)]<n-bitcnt)
            return false;
        int j;
        for(j=1;j<8;j++)
            if(set_size[N_BOWLS-((idx+1)<<3)+j]<n-bits[word&(0xff<<j)])
                goto continue_outer;

        // If there is a next word, set stuff up in preparation for that.
        if(idx < ARR_LEN - 1)
        {
            // Pick this word
            placed_so_far[idx] = word;
            // Everything that was banned so far will still be banned.
            cpy((int*)(banned[idx]), (int*)(banned[idx+1]));

            // Additionally, stuff incompatible with this word will be banned.
            banned[idx+1][idx+1] |= next_word_bans[word];

            // Also, stuff reflected from previous words across this word is
            // banned.  The number of times we can flip a over b into c,d,e is
            // the lesser of (the number of things to the left) and
            // (the number of things to the right + 1).
            // This does pollute memory at the beginning of the next
            // banned array, but that should be safe.
            for(j=1; j<=n_flips; j++)
                reflect(placed_so_far[idx-j], word, &(banned[idx+1][idx+j-1]));
#ifdef TEMPLATE_HACK
            if(dfs<idx+1>(n-bitcnt))
#else
            if(dfs(idx+1,n-bitcnt))
#endif //TEMPLATE_HACK
                return true;
        }
        else
        {
#ifdef TEMPLATE_HACK
            if(dfs<idx+1>(n-bitcnt))
                return true;
#else
            if(n==bitcnt)
                return true;
#endif //TEMPLATE_HACK
        }
continue_outer:;
    }
    return false;
}

#ifdef TEMPLATE_HACK
template <>
bool dfs<ARR_LEN>(const int n)
{
    // Terminal condition.
    return !n;
}
#endif //TEMPLATE_HACK

bool compare(uchar i, uchar j)
{
    if(bits[i] != bits[j])
        return !(bits[i]<bits[j]);
    return !(i<j);
}

int main()
{
    uchar junk[41]={216, 209, 204, 202, 198, 197, 195, 180, 177,
    166, 165, 163, 153, 141, 139, 208, 200, 196, 194, 193, 176, 164, 162, 161, 152,
    148, 145, 140, 138, 137, 134, 133, 131, 192, 160, 144, 136, 132, 130, 129, 128};
    for(int i=0;i<41;i++)
        first_level_array.push_back(junk[i]);

    memset(&placed_so_far, 0, ARR_LEN);
    memset(&banned, 0, ARR_LEN * ARR_LEN);
    memset(&next_word_bans, 0, 256);

    bits[0]=0;
    for(int i=1; i<256; ++i)
    {
        // i & (i-1) is all of i other than the lowest bit.
        bits[i] = bits[i & (i-1)] + 1;
    }

    // At shift = 4 we get 100010001 which is too many bits ;(
    for(int shift = 1; shift < 4; ++shift)
    {
        int progression = 1 | (1<<shift) | (1<<(shift*2));
        while(progression < 256)
        {
            byte_3AP.push_back(progression);
            progression<<=1;
        }
    }

    for(int candidate = 0; candidate < 256; ++candidate)
    {
        bool cntinue = false;
        for(int i=0; i<byte_3AP.size(); ++i)
            cntinue |= ((candidate & byte_3AP[i]) == byte_3AP[i]);
        if(cntinue)
            continue;
        // If we get here, it didn't contain any 3APs.
        three_free[0].push_back(candidate);
    }
    for(int i=1; i<256; ++i)
        for(int j=0; j<three_free[0].size(); j++)
            if((three_free[0][j] & i) == 0)
                three_free[i].push_back(three_free[0][j]);

    for(int i=0; i<8; ++i)
        for(int j=i+1; j<8; ++j)
        {
            int bad_bit_idx = j+(j-i) - 8;
            if(bad_bit_idx < 0)
                continue;
            uchar word = (HIGH_BIT >> i) | (HIGH_BIT >> j);
            uchar bad_bit = HIGH_BIT >> bad_bit_idx;
            for(int k=0; k<three_free[0].size(); ++k)
                if((three_free[0][k] & word) == word)
                    next_word_bans[three_free[0][k]] |= bad_bit;
        }

    for(int i=0;i<256;i++)
    {
        sort(three_free[i].begin(), three_free[i].end(), compare);
    }

    // If N_BOWLS isn't a multiple of 8, we can't use the last bits
    // of the last word no matter what.
    if(N_BOWLS % 8 != 0)
        banned[0][ARR_LEN-1] = 0xff >> (N_BOWLS % 8);

    // Calculate reversed things.
    rev[0] = 0;
    for(int i=0; i<8; ++i)
        rev[1 << i] = 128 >> i;
    for(int i=0; i<256; ++i)
    {
        int one_bit = (i&(-i));
        int the_rest = i^one_bit;
        rev[i] = rev[one_bit] | rev[the_rest];
    }

    ptime start(microsec_clock::universal_time());

#ifdef TEMPLATE_HACK
    int result = dfs<0>(N_ORANGES);
#else
    int result = dfs(0,N_ORANGES);
#endif

    ptime end(microsec_clock::universal_time());

    // Print the answer.
    printf("%d\n",result);

    long dif = (end-start).total_milliseconds();
    printf("Took %ld milliseconds to look for set of size %d in %d.\n",
            dif, N_ORANGES, N_BOWLS);
}
