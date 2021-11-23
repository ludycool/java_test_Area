package com.ludy.leetcode.array;

public class arraytest {

    public static void main(String[] args) {


        /**
         int[] nums=new int[]{0,1,1,1,1,2,2,3,3,7};
        int max1=removeDuplicates(nums);
        System.out.print(max1);
        System.out.print(nums);
        */
        int[] prices=new int[]{7,1,5,3,6,4};
        int profit=maxProfit(prices);
        System.out.print(profit);
    }



    /**
     * 给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。
     *
     * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
     *
     * 作者：力扣 (LeetCode)
     * 链接：https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/x2gy9m/
     * 来源：力扣（LeetCode）
     * @param nums
     * @return
     */
     static int removeDuplicates(int[] nums) {
            if(nums==null||nums.length==0)
                    return  0;
            int left=0;
            for (int right=1;right<nums.length;right++)
            {
                if(nums[left]!=nums[right])
                {
                    nums[++left]=nums[right];
                }
            }
            return  ++left;
    }

    /**
     * 买卖股票的最佳时机 II
     * 给定一个数组 prices ，其中 prices[i] 是一支给定股票第 i 天的价格。
     *
     * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
     *
     * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
     * @param prices
     * @return
     */
    static int maxProfit(int[] prices) {
        if(prices==null||prices.length==0)
            return  0;

        int profit=0;
        boolean isHave=false;
        for (int i=1;i<prices.length;i++)
        {
            if(prices[i]<prices[i-1])//降
            {
                if(isHave)//如果有货，就要卖
                    profit=profit+prices[i-1];
                isHave=false;

            }else  if(!isHave){//涨，如果没货就要买
                    profit=profit-prices[i-1];
                isHave=true;
            }
        }
        if(isHave)//如果还有，就卖了吧
        {
            profit=profit+prices[prices.length-1];
        }
            return  profit;
    }
}
