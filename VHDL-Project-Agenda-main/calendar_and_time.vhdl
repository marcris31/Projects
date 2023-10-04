library IEEE;
use IEEE.STD_LOGIC_1164.ALL;													 
use IEEE.NUMERIC_STD.ALL;								 
use ieee.std_logic_unsigned.all;

entity  calendar_orar is 
	port (
		clk : in std_logic;	
		reset : in std_logic; --when 0, saves data, when 1, changes are made
		minute_1 : in std_logic_vector(3 downto 0);
		minute_0: in std_logic_vector(3 downto 0);
		hour_1 : in std_logic_vector(3 downto 0); 
		hour_0 : in std_logic_vector(3 downto 0);
		day_in_1 : in std_logic_vector(3 downto 0); 
		day_in_0 : in std_logic_vector(3 downto 0);
		month_in_1 : in std_logic_vector(3 downto 0); 
		month_in_0 : in std_logic_vector(3 downto 0);
		year_in_1 : in std_logic_vector(3 downto 0); 
		year_in_0 : in std_logic_vector(3 downto 0);
		
		day_lett: out string (3 downto 1);
		minute_out_1: out std_logic_vector(3 downto 0);
		minute_out_0: out std_logic_vector(3 downto 0);
		hour_out_1:	out std_logic_vector(3 downto 0);
		hour_out_0:	out std_logic_vector(3 downto 0);
		day_out_1 : out std_logic_vector(3 downto 0); 
		day_out_0 : out std_logic_vector(3 downto 0);
		month_out_1 : out std_logic_vector(3 downto 0); 
		month_out_0 : out std_logic_vector(3 downto 0);
		year_out_1 : out std_logic_vector(3 downto 0); 
		year_out_0 : out std_logic_vector(3 downto 0));
end;

architecture arch_cal_clk of calendar_orar is 
	signal an_a1: std_logic_vector(3 downto 0):= "0000";
	signal an_a0: std_logic_vector(3 downto 0):= "0000";
	signal m_a1: std_logic_vector(3 downto 0):= "0000";
	signal m_a0: std_logic_vector(3 downto 0):= "0001";
	signal d_a1: std_logic_vector(3 downto 0):= "0000";
	signal d_a0: std_logic_vector(3 downto 0):= "0001";
	signal h1: std_logic_vector(3 downto 0):= "0000";
	signal h0: std_logic_vector(3 downto 0):= "0000";
	signal m1: std_logic_vector(3 downto 0):= "0000";
	signal m0: std_logic_vector(3 downto 0):= "0000";
	type table is array (1 to 12) of integer;  --The Key Value Method 
											   -- https://beginnersbook.com/2013/04/calculating-day-given-date/
	type dtab is array (0 to 6) of string (3 downto 1);
begin 
	process (clk, reset)
		variable i: integer; 
	begin	  
		--i := to_integer(unsigned(an_a1*10+an_a0));
		if 	reset ='1' then 
			m1<= minute_1;
			m0<= minute_0;
			h1<= hour_1;
			h0<= hour_0;
			an_a1<= year_in_1;
			an_a0<= year_in_0;
			m_a1<= month_in_1;
			m_a0<= month_in_0; 
			d_a1<= day_in_1; 
			d_a0<= day_in_0;
		else 
			IF rising_edge(clk) then
				--change minute units
				m0<= m0 + "0001";
				--change minute tens
				if m0= "1001" then
					m0<= "0000";
					m1<= m1 + "0001";
				end if;	
				-- change hour units
				if m1 = "0101" and m0 = "1001" then 
					m1<="0000";
					m0<= "0000";
					h0 <=h0+ "0001";
				END IF;	
				--change hour tens
				if 	m1 = "0101" and m0 = "1001" and h0 = "1001" THEN
					h0<="0000";
					h1<=h1+"0001";
				end if;
				--change day units
				if m1 = "0101" and m0 = "1001" and h0 = "0011" and h1 = "0010" then
					h1<="0000";
					h0<="0000";
					d_a0<=d_a0+"0001";
				end if;
				--change day tens
				if m1 = "0101" and m0 = "1001" and h0 = "0011" and h1 = "0010" and d_a0 ="1001" then
					d_a0<= "0000";
					d_a1<= d_a1+"0001";
				end if;
				-- change month units, depending on the nr of days 
				-- for 31 days
				if (m_a0= x"1" and m_a1=x"0") or (m_a0= x"3" and m_a1=x"0") or (m_a0= x"5" and m_a1=x"0") or (m_a0= x"7" and m_a1=x"0") or (m_a0= x"8" and m_a1=x"0") or (m_a0= x"0" and m_a1=x"1") or (m_a0= x"2" and m_a1=x"1") then  
					if m1 = "0101" and m0 = "1001" and h0 = "0011" and h1 = "0010" and d_a0 ="0001"	AND d_a1 = "0011" then
						m_a0<= m_a0+"0001";
						d_a1<="0000";
						d_a0<= "0001";
					end if;
					-- for february
				elsif m_a0 ="0010" and m_a1="0000" then	
					i := to_integer(unsigned (an_a0 + (an_a1*"1010")));
					if i mod 4 = 0 then 
						if m1 = "0101" and m0 = "1001" and h0 = "0011" and h1 = "0010" and d_a0 ="1001"	AND d_a1 = "0010" then
							m_a0<= m_a0+"0001";
							d_a1<="0000";
							d_a0<= "0001";
						end if;
					else 
						if m1 = "0101" and m0 = "1001" and h0 = "0011" and h1 = "0010" and d_a0 ="1000"	AND d_a1 = "0010" then
							m_a0<= m_a0+"0001";
							d_a1<="0000";
							d_a0<= "0001";
						end if;
					end if;
					-- for 30 days
				elsif (m_a0= x"4" and m_a1=x"0") or (m_a0= x"6" and m_a1=x"0") or (m_a0= x"9" and m_a1=x"0") or (m_a0= x"1" and m_a1=x"1") then   
					if m1 = "0101" and m0 = "1001" and h0 = "0011" and h1 = "0010" and d_a0 ="0000"	AND d_a1 = "0011" then
						m_a0<= m_a0+"0001";
						d_a1<="0000";
						d_a0<= "0001";
					end if;
				end if;
				-- change month tens
				if m1 = "0101" and m0 = "1001" and h0 = "0011" and h1 = "0010" and d_a0 ="0001"	AND d_a1 = "0011" and m_a0 = "0010" then 
					m_a1<= "0001";
					m_a0<="0000";
				end if;
				-- change year units
				if m1 = "0101" and m0 = "1001" and h0 = "0011" and h1 = "0010" and d_a0 ="0001"	AND d_a1 = "0011" and m_a0 = "0010" and m_a1="0001" then 
					m_a1<= "0000";
					m_a0<= "0001";
					an_a0<=an_a0+"0001";
				end if;	   
				-- change year tens
				if m1 = "0101" and m0 = "1001" and h0 = "0011" and h1 = "0010" and d_a0 ="0001"	AND d_a1 = "0011" and m_a0 = "0010" and m_a1="0001" and an_a0 ="1001" then
					an_a0<="0000";
					an_a1<= an_a1+"0001";
				end if;
			end if;
		end if;
	end process;   
	FIND_DAY_OF_WEEK: process (m0)
		variable k, j: integer;
		variable t: table;
		variable dtb: dtab;
	begin
		j:=to_integer(unsigned(an_a1*"1010"+ an_a0));  --Take the last 2 digits of the year.
		k:=j/4;										   --Divide it by 4 and discard any remainder.
		k:=k+to_integer(unsigned(d_a0+d_a1*"1010" ));  --Add the day of the month to the value obtained in step 2.
		t(1):=1;
		t(2):=4;
		t(3):=4;
		t(4):=0;
		t(5):=2;
		t(6):=5;
		t(7):=0;
		t(8):=3;
		t(9):=6;
		t(10):=1;
		t(11):=4;
		t(12):=6;
		dtb(0):= "SAT";
		dtb(1):= "SUN";
		dtb(2):= "MON";
		dtb(3):= "TUE";
		dtb(4):= "WEN";
		dtb(5):= "THU";
		dtb(6):= "FRI";
		k:=k+t(to_integer(unsigned(m_a0+m_a1*"1010" ))); -- Add the month’s key value, from the following table to the value obtained in step 3.
														 
		if to_integer(unsigned(m_a0+m_a1*"1010")) <3 then-- If the date is in January or February of a leap year, subtract 1 from step 4.
			k:=k-1;
		end if;
		k:=k+6; --  it works for years 2000 to 2099
				--	if we pass the year 2099, the day won't be anymore relevant
		k:= k+j;-- Add the last two digits of the year to the value we obtained in the previous step
		k:= k mod 7;  --Divide this value by 7 and take the remainder. Get the day from the following table based on the value of the remainder
		day_lett<= dtb(k); 
	end process;		   
	
	minute_out_1<= m1;
	minute_out_0  <= m0;
	hour_out_1 <= h1;
	hour_out_0 <= h0;
	day_out_1  <= d_a1;
	day_out_0  <= d_a0;
	month_out_1 <= m_a1; 
	month_out_0	<= m_a0;
	year_out_1 <= an_a1;	
	year_out_0 <= an_a0;
	
end;
