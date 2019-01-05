# Gamewuziqi

					版权声明：本文为博主原创文章，未经博主允许不得转载。					https://blog.csdn.net/huijiaaa1/article/details/85807681				</div>
								            <div id="content_views" class="markdown_views prism-atom-one-light">
							<!-- flowchart 箭头图标 勿删 -->
							<svg xmlns="http://www.w3.org/2000/svg" style="display: none;"><path stroke-linecap="round" d="M5,0 0,2.5 5,5z" id="raphael-marker-block" style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0);"></path></svg>
							<h1><a name="t0"></a><a id="_java_0" target="_blank"></a>五子棋网络对战 java实现</h1>
<pre class="prettyprint"><code class="has-numbering">	最近期末考试，压力实在是太大了。专业不对口的痛苦，别人永远体会不来。
	闲暇之余，便想着巩固一下java的基础知识，以前看别人写的游戏，心里都觉得哇 好厉害啊，我什么
	时候才可以写出自己的游戏，当然我最想做的不是游戏，因为我感觉我只有欣赏美的能力，
	却没有制造美的能力。好了，废话这么多，哎，我就是喜欢啰嗦。

</code><ul class="pre-numbering" style=""><li style="color: rgb(153, 153, 153);">1</li><li style="color: rgb(153, 153, 153);">2</li><li style="color: rgb(153, 153, 153);">3</li><li style="color: rgb(153, 153, 153);">4</li><li style="color: rgb(153, 153, 153);">5</li></ul></pre>
<h3><a name="t1"></a><a id="font_colorredfont_12" target="_blank"></a><font color="red">游戏消息传输构思：</font></h3>
<p>	<font color="#0099ff" face="楷体" bgcolor="#FF4500">首先呢，写一个程序，最重要的就是构思啦，那么首先想到的是网络实现的方式，在这里我们可以用http协议去传输我们的数据，也可以采用封装的TCP/ip的socket去实现我们想要的功能，在网络这个世界，一切皆socket,包括你熟知的http协议，底层传输数据也是socket连接传输的，学过java的都知道Tomcat这个web服务器吧，那么这就好了，Tomcat给客户端传输数据也是socket连接的，你也可以通过socket实现自己的web服务器了，好了，传输协议确定了，那么接下来就要考虑游戏构思了</font></p>
<h3><a name="t2"></a><a id="font_colorredfont_16" target="_blank"></a><font color="red">游戏构思：</font></h3>
<p>	<font color="#0099ff" face="楷体" bgcolor="#FF4500">既然要写网络游戏嘛，那么肯定要有一个客户端，一个服务端对吧。这是最典型的C/S(Client/Server)，对于WEB而言的话，那么是B/S(Browser/Server).那么我们就选择一个最简单的C/S模型吧。</font></p>
<h4><a id="font_color7FFFD41_Serverfont_20" target="_blank"></a><font color="#7FFFD4">1. Server实现大致思路</font></h4>
<p>	<font color="#0099ff" face="楷体" bgcolor="#FF4500">然后呢，服务端接受客户端的连接，然后保存每一个客户端连接的socket对象，每连接一个客户端，则开启一个线程去读当前客户端消息的线程，然后定义我们游戏的消息头，消息头很重要，能够微量防止不法连接。然后就是对消息体的一些处理逻辑了。到这里先不要考虑连接数，实际上，这么写是最笨的做法，想想，万一有一万个连接，难道就要开一万个线程去读每个socket客户端的消息吗？对于很大的连接数，这里就不要考虑了，我们只追求基本功能能够实现即可，刚开始不要考虑那么多。你只要知道这是一个最简单的游戏的制作过程</font></p>
<p><font color="#0000FF">Server  implements ：（伪代码）</font></p>
<pre class="prettyprint"><code class="prism language-java has-numbering" onclick="mdcp.copyCode(event)"><span class="token keyword">class</span> <span class="token class-name">Server</span> <span class="token punctuation">{</span>
    <span class="token keyword">static</span> Map<span class="token generics function"><span class="token punctuation">&lt;</span>Socket<span class="token punctuation">,</span>String<span class="token punctuation">&gt;</span></span> Clients <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">HashMap</span><span class="token operator">&lt;</span><span class="token operator">&gt;</span><span class="token punctuation">(</span>Socket<span class="token punctuation">,</span>String<span class="token punctuation">)</span><span class="token punctuation">;</span>
    ServerSocket serverSocket<span class="token punctuation">;</span>
    <span class="token function">main</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
        <span class="token comment">//监听本机6666端口</span>
        serverSocket <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">ServerSocket</span><span class="token punctuation">(</span><span class="token number">6666</span><span class="token punctuation">)</span>
        Client <span class="token operator">=</span>  serverSocket<span class="token punctuation">.</span><span class="token function">accpet</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span><span class="token comment">//服务端开启监听</span>
        <span class="token keyword">new</span> <span class="token class-name">HandleClient</span><span class="token punctuation">(</span>Client<span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">start</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span><span class="token comment">//启动当前客户端监听</span>
        <span class="token comment">//连接上则添加到map列表，名字默认为空</span>
        Clients<span class="token punctuation">.</span><span class="token function">put</span><span class="token punctuation">(</span>Client<span class="token punctuation">,</span><span class="token string">""</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
<span class="token keyword">class</span> <span class="token class-name">HandleClient</span> <span class="token keyword">extends</span> <span class="token class-name">Thread</span><span class="token punctuation">{</span>
​    Socket Client<span class="token punctuation">;</span>
​    BufferedReader br<span class="token punctuation">;</span><span class="token comment">//当前Socket的Reader对象</span>
​    BufferedWriter bw<span class="token punctuation">;</span><span class="token comment">//当前Socket的Wirter对象</span>
​    BufferedWriter otherbw <span class="token comment">//另一个玩家的writer对象</span>
​    <span class="token function">HandleClient</span><span class="token punctuation">(</span>Socket Client<span class="token punctuation">)</span><span class="token punctuation">{</span>
​        <span class="token keyword">this</span><span class="token punctuation">.</span>Client <span class="token operator">=</span> Client<span class="token punctuation">;</span>
​	<span class="token punctuation">}</span>
​    <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">run</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​        bw <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">BufferedWriter</span><span class="token punctuation">(</span><span class="token keyword">new</span> <span class="token class-name">OutputStreamWriter</span>（Client<span class="token punctuation">.</span><span class="token function">getoutputStream</span><span class="token punctuation">(</span><span class="token punctuation">)</span>）<span class="token punctuation">;</span>
​        br<span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">BufferedReader</span><span class="token punctuation">(</span><span class="token keyword">new</span> <span class="token class-name">OutputStreamReader</span>（Client<span class="token punctuation">.</span><span class="token function">getinputStream</span><span class="token punctuation">(</span><span class="token punctuation">)</span>）<span class="token punctuation">;</span>  
​                               <span class="token keyword">while</span><span class="token punctuation">(</span><span class="token boolean">true</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​                                  String msg <span class="token operator">=</span>  bf<span class="token punctuation">.</span><span class="token function">readLine</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
​                                   String msgStr<span class="token punctuation">[</span><span class="token punctuation">]</span> <span class="token operator">=</span> msg<span class="token punctuation">.</span><span class="token function">split</span><span class="token punctuation">(</span><span class="token string">":"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
​                                   <span class="token comment">//定义消息头，消息如果满足消息头</span>
​                                   <span class="token keyword">if</span><span class="token punctuation">(</span>msgStr<span class="token punctuation">[</span><span class="token number">0</span><span class="token punctuation">]</span><span class="token punctuation">.</span><span class="token function">equals</span><span class="token punctuation">(</span><span class="token string">"MSGTYPE"</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​                                       <span class="token comment">//。。。。消息解析处理过程</span>
  <span class="token comment">//对消息体用“#” 切割 拿到消息的Key,如果当前消息是发来自己的名字  ，</span>
​                                      <span class="token comment">// 那么 就把这个连接添加到Server</span>
​                                       <span class="token keyword">if</span><span class="token punctuation">(</span>msgStr<span class="token punctuation">[</span><span class="token number">1</span><span class="token punctuation">]</span><span class="token punctuation">.</span><span class="token function">splint</span><span class="token punctuation">(</span><span class="token string">"#"</span><span class="token punctuation">)</span><span class="token punctuation">[</span><span class="token number">0</span><span class="token punctuation">]</span><span class="token punctuation">.</span><span class="token function">equals</span><span class="token punctuation">(</span><span class="token string">"MYNAME"</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​                            <span class="token comment">//更新改Socket玩家的姓名</span>
​                                           Server<span class="token punctuation">.</span>Clients<span class="token punctuation">.</span><span class="token function">put</span><span class="token punctuation">(</span>Client<span class="token punctuation">,</span>msgStr<span class="token punctuation">[</span><span class="token number">1</span><span class="token punctuation">]</span><span class="token punctuation">.</span><span class="token function">splint</span><span class="token punctuation">(</span><span class="token string">"#"</span><span class="token punctuation">)</span><span class="token punctuation">[</span><span class="token number">1</span><span class="token punctuation">]</span><span class="token punctuation">)</span><span class="token punctuation">;</span>   
​                                           <span class="token comment">//接下来 的代码就不写了 直接看我的工程即可，服务端游戏思路大致就是这样。就是这样切割消息体来进入相应的处理</span>
​                                       <span class="token punctuation">}</span>
​                                   <span class="token punctuation">}</span>
​                               <span class="token punctuation">}</span>
​	<span class="token punctuation">}</span>
<span class="token punctuation">}</span>
<div class="hljs-button" data-title="复制"></div></code><ul class="pre-numbering" style=""><li style="color: rgb(153, 153, 153);">1</li><li style="color: rgb(153, 153, 153);">2</li><li style="color: rgb(153, 153, 153);">3</li><li style="color: rgb(153, 153, 153);">4</li><li style="color: rgb(153, 153, 153);">5</li><li style="color: rgb(153, 153, 153);">6</li><li style="color: rgb(153, 153, 153);">7</li><li style="color: rgb(153, 153, 153);">8</li><li style="color: rgb(153, 153, 153);">9</li><li style="color: rgb(153, 153, 153);">10</li><li style="color: rgb(153, 153, 153);">11</li><li style="color: rgb(153, 153, 153);">12</li><li style="color: rgb(153, 153, 153);">13</li><li style="color: rgb(153, 153, 153);">14</li><li style="color: rgb(153, 153, 153);">15</li><li style="color: rgb(153, 153, 153);">16</li><li style="color: rgb(153, 153, 153);">17</li><li style="color: rgb(153, 153, 153);">18</li><li style="color: rgb(153, 153, 153);">19</li><li style="color: rgb(153, 153, 153);">20</li><li style="color: rgb(153, 153, 153);">21</li><li style="color: rgb(153, 153, 153);">22</li><li style="color: rgb(153, 153, 153);">23</li><li style="color: rgb(153, 153, 153);">24</li><li style="color: rgb(153, 153, 153);">25</li><li style="color: rgb(153, 153, 153);">26</li><li style="color: rgb(153, 153, 153);">27</li><li style="color: rgb(153, 153, 153);">28</li><li style="color: rgb(153, 153, 153);">29</li><li style="color: rgb(153, 153, 153);">30</li><li style="color: rgb(153, 153, 153);">31</li><li style="color: rgb(153, 153, 153);">32</li><li style="color: rgb(153, 153, 153);">33</li><li style="color: rgb(153, 153, 153);">34</li><li style="color: rgb(153, 153, 153);">35</li><li style="color: rgb(153, 153, 153);">36</li><li style="color: rgb(153, 153, 153);">37</li><li style="color: rgb(153, 153, 153);">38</li><li style="color: rgb(153, 153, 153);">39</li><li style="color: rgb(153, 153, 153);">40</li><li style="color: rgb(153, 153, 153);">41</li></ul></pre>
<h4><a id="font_color__7FFF00font_70" target="_blank"></a><font color="#7FFF00">思考：</font></h4>
<p>	<font color="#0099ff" face="楷体" bgcolor="#FF4500">这里为什么HandleClient类里我还写了一个  《BufferedWriter otherbw //另一个玩家的writer对象》 这句代码，你可能有所疑惑，为什么都有了一个bw，还要一个otherbw,但是你忘了吗，我们要写的五子棋是双人对战的，这里 这个服务器的当前线程，当有人加入到房间，或者是自己创建的房间，有人加入进来，拿Server的静态对象Clients找到加入房间玩家的Socket，接着拿到输出流，不就可以直接给他发消息了吗？这里要好好想想了啊，这里是重点</font></p>
<h4><a id="font_color7FFFD41_Clientfont_74" target="_blank"></a><font color="#7FFFD4">1. Client实现大致思路</font></h4>
<p>	<font color="#0099ff" face="楷体" bgcolor="#FF4500">然后，客户端实现过程当眼也很简单了啊，直接建立一个Socket对象去连接服务端，然后发送相应的消息体，服务端响应后，返回给客户端对应的消息。这里我给大家举一个完整的消息例子。 </font></p>
<p><font color="#0099ff" face="楷体" bgcolor="#FF4500">Client---------------&gt;Server （连接成功）</font></p>
<p><font color="#0099ff" face="楷体" bgcolor="#FF4500">BEGIN:</font></p>
<p><font color="#0099ff" face="楷体" bgcolor="#FF4500">发送MyName---------&gt;Server （保存到Server的Clients对象里）</font></p>
<p><font color="#0099ff" face="楷体" bgcolor="#FF4500">发送创建房间----------&gt;Server  (向服务器发送一条创建房间的消息，服务器记录当前玩家创建了房间，然后调用bw.write(“MSGTYPE:CreateRoomSuccess\r\n”))通知当前玩家建立房间成功</font></p>
<p><font color="#0099ff" face="楷体" bgcolor="#FF4500">Gameing:</font></p>
<p><font color="#0099ff" face="楷体" bgcolor="#FF4500">发送落子坐标----------&gt;Server(接受到当前玩家的落子信息，转发给另一个 玩家，怎么转发呢？前面不是直接拿到了otherbw,那么这时候直接调用加入玩家的bw即可向对方发消息。)</font></p>
<p><font color="#0000FF">Client implements ：（伪代码）</font></p>
<pre class="prettyprint"><code class="prism language-java has-numbering" onclick="mdcp.copyCode(event)"><span class="token keyword">class</span> <span class="token class-name">Client</span><span class="token punctuation">{</span>

    <span class="token function">main</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
        
        <span class="token keyword">new</span> <span class="token class-name">Room</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
<span class="token keyword">class</span> <span class="token class-name">Room</span> <span class="token keyword">extends</span> <span class="token class-name">Jframe</span><span class="token punctuation">{</span>
​    <span class="token keyword">static</span> String MyName<span class="token punctuation">;</span> <span class="token comment">//自己的名字</span>
​    <span class="token keyword">static</span> Socket socket<span class="token punctuation">;</span>
​    BufferedReader br<span class="token punctuation">;</span><span class="token comment">//当前Socket的Reader对象</span>
​    BufferedWriter bw<span class="token punctuation">;</span><span class="token comment">//当前Socket的Wirter对象</span>
​    BufferedWriter otherbw <span class="token comment">//另一个玩家的writer对象</span>
​    <span class="token function">Jframe</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​       
        <span class="token comment">//MouseLister 实现监听鼠标点击事件</span>
        <span class="token function">addMouseListener</span><span class="token punctuation">(</span><span class="token keyword">new</span> <span class="token class-name">MouseListener</span><span class="token punctuation">{</span>
                <span class="token comment">// 如果点击到某个坐标是开始游戏</span>
                <span class="token keyword">if</span><span class="token punctuation">(</span>x<span class="token operator">&gt;=</span>xxx<span class="token operator">&amp;&amp;</span>x<span class="token operator">&lt;=</span>xxx<span class="token operator">&amp;&amp;</span>y<span class="token operator">&gt;=</span>xxx<span class="token operator">&amp;&amp;</span>y<span class="token operator">&lt;=</span>xxx<span class="token punctuation">)</span><span class="token punctuation">{</span>
                	String name <span class="token operator">=</span> JOptionPane<span class="token punctuation">.</span><span class="token function">showInternalInputDialog</span><span class="token punctuation">(</span>null<span class="token punctuation">,</span><span class="token string">"请给你取一个个性的名字把！"</span><span class="token punctuation">)</span>
                        <span class="token keyword">if</span><span class="token punctuation">(</span>socket<span class="token operator">==</span>null<span class="token punctuation">)</span><span class="token punctuation">{</span>
                            <span class="token comment">//这里的localhost默认是本机的地址 </span>
                            <span class="token comment">// 如果你的Hosts文件没有定义localhost，那么改成							127.0.0.1</span>
                            socket <span class="token operator">=</span>  <span class="token keyword">new</span> <span class="token class-name">Socket</span><span class="token punctuation">(</span><span class="token string">"localhost"</span><span class="token punctuation">,</span><span class="token number">6666</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    						 bw <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">BufferedWriter</span><span class="token punctuation">(</span><span class="token keyword">new</span> <span class="token class-name">OutputStreamWriter</span>（Client<span class="token punctuation">.</span><span class="token function">getoutputStream</span><span class="token punctuation">(</span><span class="token punctuation">)</span>）<span class="token punctuation">;</span>
        br<span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">BufferedReader</span><span class="token punctuation">(</span><span class="token keyword">new</span> <span class="token class-name">OutputStreamReader</span>（Client<span class="token punctuation">.</span><span class="token function">getinputStream</span><span class="token punctuation">(</span><span class="token punctuation">)</span>）<span class="token punctuation">;</span>  
                        <span class="token punctuation">}</span>
                    		<span class="token comment">//进入游戏窗口</span>
                           <span class="token keyword">new</span> <span class="token class-name">PlayGmae</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                            <span class="token comment">//开启线程读取消息 ，为什么要开启线程读取消息？因为</span>
                            <span class="token comment">//readLine（）方法是阻塞的，要是不开线程，那么主线									程就会卡了。</span>
                            <span class="token keyword">new</span> <span class="token class-name">ReadMsg</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">start</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                       
                <span class="token punctuation">}</span>
        <span class="token punctuation">}</span><span class="token punctuation">)</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
​     <span class="token comment">//读取消息</span>
<span class="token keyword">class</span> <span class="token class-name">ReadMsg</span> <span class="token keyword">extends</span> <span class="token class-name">Thread</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​    <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">run</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​        <span class="token keyword">while</span><span class="token punctuation">(</span><span class="token boolean">true</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​           String msg <span class="token operator">=</span>  br<span class="token punctuation">.</span><span class="token function">readLine</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
​            String msgStr <span class="token operator">=</span> msg<span class="token punctuation">.</span><span class="token function">splint</span><span class="token punctuation">(</span><span class="token string">":"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
​            <span class="token keyword">if</span><span class="token punctuation">(</span>msgStr<span class="token punctuation">[</span><span class="token number">0</span><span class="token punctuation">]</span><span class="token punctuation">.</span><span class="token function">equals</span><span class="token punctuation">(</span><span class="token string">"MSGTYPE"</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​                <span class="token punctuation">.</span><span class="token punctuation">.</span><span class="token punctuation">.</span><span class="token punctuation">.</span><span class="token comment">//相关处理过程</span>
​        <span class="token punctuation">}</span>
​    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
​     <span class="token comment">// 实现相关界面</span>
​    <span class="token keyword">class</span> <span class="token class-name">PlayGame</span> <span class="token keyword">extends</span> <span class="token class-name">Jframe</span><span class="token punctuation">{</span>
​        <span class="token function">PlayGame</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​           <span class="token comment">//实现相关界面 </span>
​             <span class="token function">addMouseListener</span><span class="token punctuation">(</span>
​                 <span class="token keyword">new</span> <span class="token class-name">MouseListener</span><span class="token punctuation">{</span>
​                     <span class="token comment">//增加点击 区域 消息监听 比如不同区域 有不同的功能啊</span>
​                     <span class="token comment">// 开始游戏 棋盘 认输 悔棋等等 都有自己的坐标信息啊。点击执行相关的功能</span>
​                     
                     <span class="token comment">// eg ：当前区域 ：认输</span>
                     <span class="token keyword">if</span><span class="token punctuation">(</span>x<span class="token operator">&gt;=</span>xxx<span class="token operator">&amp;&amp;</span>x<span class="token operator">&lt;=</span>xxx<span class="token operator">&amp;&amp;</span>y<span class="token operator">&gt;=</span>xxx<span class="token operator">&amp;&amp;</span>y<span class="token operator">&lt;=</span>xxx<span class="token punctuation">)</span><span class="token punctuation">{</span>
                       <span class="token keyword">int</span> staus<span class="token operator">=</span>   JOptionPane<span class="token punctuation">.</span><span class="token function">showInternalConfirmDialog</span><span class="token punctuation">(</span><span class="token keyword">this</span><span class="token punctuation">,</span> 
<span class="token string">"确认要认输吗"</span><span class="token punctuation">,</span> ）<span class="token punctuation">;</span>
​                                                                          <span class="token keyword">if</span><span class="token punctuation">(</span>staus<span class="token operator">==</span><span class="token number">0</span><span class="token punctuation">)</span><span class="token punctuation">{</span>
​                                                                              bw<span class="token punctuation">.</span><span class="token function">write</span><span class="token punctuation">(</span><span class="token string">"MSGTYPE:GAMEING#renshu"</span><span class="token punctuation">)</span>；
​                                                                          <span class="token punctuation">}</span>
​                 <span class="token punctuation">}</span>
​             <span class="token punctuation">)</span>；
​        <span class="token punctuation">}</span>
​    <span class="token punctuation">}</span>
<div class="hljs-button" data-title="复制"></div></code><ul class="pre-numbering" style=""><li style="color: rgb(153, 153, 153);">1</li><li style="color: rgb(153, 153, 153);">2</li><li style="color: rgb(153, 153, 153);">3</li><li style="color: rgb(153, 153, 153);">4</li><li style="color: rgb(153, 153, 153);">5</li><li style="color: rgb(153, 153, 153);">6</li><li style="color: rgb(153, 153, 153);">7</li><li style="color: rgb(153, 153, 153);">8</li><li style="color: rgb(153, 153, 153);">9</li><li style="color: rgb(153, 153, 153);">10</li><li style="color: rgb(153, 153, 153);">11</li><li style="color: rgb(153, 153, 153);">12</li><li style="color: rgb(153, 153, 153);">13</li><li style="color: rgb(153, 153, 153);">14</li><li style="color: rgb(153, 153, 153);">15</li><li style="color: rgb(153, 153, 153);">16</li><li style="color: rgb(153, 153, 153);">17</li><li style="color: rgb(153, 153, 153);">18</li><li style="color: rgb(153, 153, 153);">19</li><li style="color: rgb(153, 153, 153);">20</li><li style="color: rgb(153, 153, 153);">21</li><li style="color: rgb(153, 153, 153);">22</li><li style="color: rgb(153, 153, 153);">23</li><li style="color: rgb(153, 153, 153);">24</li><li style="color: rgb(153, 153, 153);">25</li><li style="color: rgb(153, 153, 153);">26</li><li style="color: rgb(153, 153, 153);">27</li><li style="color: rgb(153, 153, 153);">28</li><li style="color: rgb(153, 153, 153);">29</li><li style="color: rgb(153, 153, 153);">30</li><li style="color: rgb(153, 153, 153);">31</li><li style="color: rgb(153, 153, 153);">32</li><li style="color: rgb(153, 153, 153);">33</li><li style="color: rgb(153, 153, 153);">34</li><li style="color: rgb(153, 153, 153);">35</li><li style="color: rgb(153, 153, 153);">36</li><li style="color: rgb(153, 153, 153);">37</li><li style="color: rgb(153, 153, 153);">38</li><li style="color: rgb(153, 153, 153);">39</li><li style="color: rgb(153, 153, 153);">40</li><li style="color: rgb(153, 153, 153);">41</li><li style="color: rgb(153, 153, 153);">42</li><li style="color: rgb(153, 153, 153);">43</li><li style="color: rgb(153, 153, 153);">44</li><li style="color: rgb(153, 153, 153);">45</li><li style="color: rgb(153, 153, 153);">46</li><li style="color: rgb(153, 153, 153);">47</li><li style="color: rgb(153, 153, 153);">48</li><li style="color: rgb(153, 153, 153);">49</li><li style="color: rgb(153, 153, 153);">50</li><li style="color: rgb(153, 153, 153);">51</li><li style="color: rgb(153, 153, 153);">52</li><li style="color: rgb(153, 153, 153);">53</li><li style="color: rgb(153, 153, 153);">54</li><li style="color: rgb(153, 153, 153);">55</li><li style="color: rgb(153, 153, 153);">56</li><li style="color: rgb(153, 153, 153);">57</li><li style="color: rgb(153, 153, 153);">58</li><li style="color: rgb(153, 153, 153);">59</li><li style="color: rgb(153, 153, 153);">60</li><li style="color: rgb(153, 153, 153);">61</li><li style="color: rgb(153, 153, 153);">62</li><li style="color: rgb(153, 153, 153);">63</li><li style="color: rgb(153, 153, 153);">64</li><li style="color: rgb(153, 153, 153);">65</li><li style="color: rgb(153, 153, 153);">66</li><li style="color: rgb(153, 153, 153);">67</li><li style="color: rgb(153, 153, 153);">68</li></ul></pre>
<h3><a name="t3"></a><a id="font_colorredfont_167" target="_blank"></a><font color="red">游戏截图：</font></h3>
<p><font color="#7FFFD4">1. Server</font></p>
<p><font color="#0099ff" face="楷体" bgcolor="#FF4500">Server我没有写界面直接启动就好了，不过要想观察后台信息，在控制台输入 "java -jar Server.jar"即可</font><br>
<font color="#0e12ff" face="楷体" bgcolor="#FF4500">服务端后台消息：</font><br>
<img src="https://img-blog.csdnimg.cn/20190104231656827.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1aWppYWFhMQ==,size_16,color_FFFFFF,t_70" alt="在这里插入图片描述"><br>
<font color="#7FFFD4">2.Client</font><br>
<font color="#0099ff" face="楷体" bgcolor="#FF4500">直接双击打开即可，也可以在控制台输入 "java -jar Client.jar"即可观察后台信息</font><br>
<font color="#0e12ff" face="楷体" bgcolor="#FF4500">Begin:</font><br>
<img src="https://img-blog.csdnimg.cn/20190104230951756.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1aWppYWFhMQ==,size_16,color_FFFFFF,t_70" alt="在这里插入图片描述"><br>
<font color="#0e12ff" face="楷体" bgcolor="#FF4500">取名字：</font><br>
<img src="https://img-blog.csdnimg.cn/20190104231513366.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1aWppYWFhMQ==,size_16,color_FFFFFF,t_70" alt="在这里插入图片描述"><br>
<font color="#0e12ff" face="楷体" bgcolor="#FF4500">空房间列表：</font><br>
<img src="https://img-blog.csdnimg.cn/20190104231104499.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1aWppYWFhMQ==,size_16,color_FFFFFF,t_70" alt="在这里插入图片描述"><br>
<font color="#0e12ff" face="楷体" bgcolor="#FF4500">创建的房间</font><br>
<img src="https://img-blog.csdnimg.cn/20190104231123170.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1aWppYWFhMQ==,size_16,color_FFFFFF,t_70" alt="在这里插入图片描述"><br>
<font color="#0e12ff" face="楷体" bgcolor="#FF4500">有房间：</font><br>
<img src="https://img-blog.csdnimg.cn/20190104231155121.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1aWppYWFhMQ==,size_16,color_FFFFFF,t_70" alt="在这里插入图片描述"><br>
<font color="#0e12ff" face="楷体" bgcolor="#FF4500">加入房间玩家：</font><br>
<img src="https://img-blog.csdnimg.cn/20190104231232610.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1aWppYWFhMQ==,size_16,color_FFFFFF,t_70" alt="在这里插入图片描述"><br>
<font color="#0e12ff" face="楷体" bgcolor="#FF4500">开始游戏：</font><br>
<img src="https://img-blog.csdnimg.cn/20190104231322234.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1aWppYWFhMQ==,size_16,color_FFFFFF,t_70" alt="在这里插入图片描述"><br>
<font color="#0e12ff" face="楷体" bgcolor="#FF4500">聊天内容：</font><br>
<img src="https://img-blog.csdnimg.cn/20190104231439283.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1aWppYWFhMQ==,size_16,color_FFFFFF,t_70" alt="在这里插入图片描述"><br>
<font color="#0e12ff" face="楷体" bgcolor="#FF4500">关于作者：</font><br>
<img src="https://img-blog.csdnimg.cn/20190104231415164.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1aWppYWFhMQ==,size_16,color_FFFFFF,t_70" alt="在这里插入图片描述"></p>
<h3><a name="t4"></a><a id="font_colorredfont_194" target="_blank"></a><font color="red">总结：</font></h3>
<p><font color="#0099ff" face="楷体" bgcolor="#FF4500">好了，这就是游戏实现的大致思路，感觉就是这么一步一步来的，这个游戏断断续续，都怪考试太多了，哎，我写了两周才完成了所有功能…，一边复习，一边考试。其实大多时间都再调试BUG，要么是点击出问题，要么是创建房间加入出问题…各种问题，当然，要想实现自己想要的游戏，努力一下还是挺好的。到现在为止，游戏可以创建多个房间，如果游戏厅房间太多则多页显示，可以自由加入任何创建的房间</font></p>
<p><font color="#7FFFD4">关于待补充的：</font></p>
<p><font color="#0099ff" face="楷体" bgcolor="#FF4500">游戏还有 很多要完善的地方，比如 房间观战，送♥动画特效，人机对战，哎，有时间我再完善，没时间，这个小项目就到这里，新的2019，要继续努力，送给每一个看到此篇文章的人，愿你们都能开心过好每一天。</font></p>

            </div>
    					<link href="https://csdnimg.cn/release/phoenix/mdeditor/markdown_views-2011a91181.css" rel="stylesheet">
                </div>