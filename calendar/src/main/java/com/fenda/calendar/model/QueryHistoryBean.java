package com.fenda.calendar.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/10/10
 * @Describe: 查询历史事件实体
 */
public class QueryHistoryBean {

    /**
     * recordId : f91576cddb46468b9c6c41e2054c5824
     * skillId : 2019031800001161
     * requestId : f91576cddb46468b9c6c41e2054c5824
     * nlu : {"skillId":"2019031800001161","res":"5d8ae7040d32eb000d70f883","input":"历史上五月一号有什么大事发生","inittime":34.682861328125,"loadtime":68.33203125,"skill":"日历","skillVersion":"38","source":"dui","systime":341.64721679687,"semantics":{"request":{"slots":[{"pos":[3,6],"rawvalue":"五月一号","name":"阳历日期","rawpinyin":"wu yue yi hao","value":"20190501"},{"name":"intent","value":"查询历史事件"}],"task":"日历","confidence":1,"slotcount":2,"rules":{"5d8ae7040d32eb000d70fac1":{"阳历日期":["20190501",3,6],"intent":["查询历史事件",-1,-1]}}}},"version":"2019.1.15.20:40:58","timestamp":1570692358}
     * dm : {"input":"历史上五月一号有什么大事发生","shouldEndSession":true,"task":"日历","widget":{"widgetName":"default","duiWidget":"text","subTitle":"历史上的1773年5月1日，清朝开设《四库全书》编纂馆。<br>","extra":{"result":[{"date":"17070501","month":5,"subTitle":"英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","year":1707,"title":"1707年，英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","event":"1707年，英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","day":1},{"date":"17730501","month":5,"subTitle":"清朝开设《四库全书》编纂馆。","year":1773,"title":"1773年，清朝开设《四库全书》编纂馆。","event":"1773年，清朝开设《四库全书》编纂馆。","day":1},{"date":"17900501","month":5,"subTitle":"美国完成第一次人口普查。","year":1790,"title":"1790年，美国完成第一次人口普查。","event":"1790年，美国完成第一次人口普查。","day":1},{"date":"18340501","month":5,"subTitle":"奴隶制在英国领土范围内被废除。","year":1834,"title":"1834年，奴隶制在英国领土范围内被废除。","event":"1834年，奴隶制在英国领土范围内被废除。","day":1},{"date":"18400501","month":5,"subTitle":"英国正式发行世界上首枚邮票黑便士正面为维多利亚女王的头像。","year":1840,"title":"1840年，英国正式发行世界上首枚邮票黑便士，正面为维多利亚女王的头像。","event":"1840年，英国正式发行世界上首枚邮票黑便士，正面为维多利亚女王的头像。","day":1},{"date":"18510501","month":5,"subTitle":"第一届国际博览会在英国伦敦开幕。","year":1851,"title":"1851年，第一届国际博览会在英国伦敦开幕。","event":"1851年，第一届国际博览会在英国伦敦开幕。","day":1},{"date":"18860501","month":5,"subTitle":"美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱要求八小时工作制。","year":1886,"title":"1886年，美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱，要求八小时工作制。","event":"1886年，美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱，要求八小时工作制。","day":1},{"date":"18940501","month":5,"subTitle":"甲午战争：日本宣布对中国开战。","year":1894,"title":"1894年，甲午战争：日本宣布对中国开战。","event":"1894年，甲午战争：日本宣布对中国开战。","day":1},{"date":"18980501","month":5,"subTitle":"美西战争：美国海军占领马尼拉。","year":1898,"title":"1898年，美西战争：美国海军占领马尼拉。","event":"1898年，美西战争：美国海军占领马尼拉。","day":1},{"date":"19140501","month":5,"subTitle":"袁世凯颁布《中华民国约法》。","year":1914,"title":"1914年，袁世凯颁布《中华民国约法》。","event":"1914年，袁世凯颁布《中华民国约法》。","day":1},{"date":"19170501","month":5,"subTitle":"督军团欲迫政府对德宣战。","year":1917,"title":"1917年，督军团欲迫政府对德宣战。","event":"1917年，督军团欲迫政府对德宣战。","day":1},{"date":"19220501","month":5,"subTitle":"第一次全国劳动大会召开。","year":1922,"title":"1922年，第一次全国劳动大会召开。","event":"1922年，第一次全国劳动大会召开。","day":1},{"date":"19260501","month":5,"subTitle":"英国大罢工爆发。","year":1926,"title":"1926年，英国大罢工爆发。","event":"1926年，英国大罢工爆发。","day":1},{"date":"19270501","month":5,"subTitle":"广东海丰、陆丰农民起义。","year":1927,"title":"1927年，广东海丰、陆丰农民起义。","event":"1927年，广东海丰、陆丰农民起义。","day":1},{"date":"19310501","month":5,"subTitle":"当时世界上最高的建筑纽约帝国大厦剪彩。","year":1931,"title":"1931年，当时世界上最高的建筑纽约帝国大厦剪彩。","event":"1931年，当时世界上最高的建筑纽约帝国大厦剪彩。","day":1},{"date":"19330501","month":5,"subTitle":"国民党部署第五次对苏区的\u201c围剿\u201d。","year":1933,"title":"1933年，国民党部署第五次对苏区的\u201c围剿\u201d。","event":"1933年，国民党部署第五次对苏区的\u201c围剿\u201d。","day":1},{"date":"19350501","month":5,"subTitle":"红军抢渡金沙江。","year":1935,"title":"1935年，红军抢渡金沙江。","event":"1935年，红军抢渡金沙江。","day":1},{"date":"19390501","month":5,"subTitle":"中国军队反攻南昌失利。","year":1939,"title":"1939年，中国军队反攻南昌失利。","event":"1939年，中国军队反攻南昌失利。","day":1},{"date":"19410501","month":5,"subTitle":"美国《公民凯恩》影片上映。","year":1941,"title":"1941年，美国《公民凯恩》影片上映。","event":"1941年，美国《公民凯恩》影片上映。","day":1},{"date":"19480501","month":5,"subTitle":"毛泽东提议召开新政协会议得到各界响应。","year":1948,"title":"1948年，毛泽东提议召开新政协会议得到各界响应。","event":"1948年，毛泽东提议召开新政协会议得到各界响应。","day":1},{"date":"19500501","month":5,"subTitle":"解放军解放海南岛。","year":1950,"title":"1950年，解放军解放海南岛。","event":"1950年，解放军解放海南岛。","day":1},{"date":"19500501","month":5,"subTitle":"中央号召开展大规模整风运动。","year":1950,"title":"1950年，中央号召开展大规模整风运动。","event":"1950年，中央号召开展大规模整风运动。","day":1},{"date":"19570501","month":5,"subTitle":"戚烈云破游泳世界纪录。","year":1957,"title":"1957年，戚烈云破游泳世界纪录。","event":"1957年，戚烈云破游泳世界纪录。","day":1},{"date":"19580501","month":5,"subTitle":"人民英雄纪念碑揭幕。","year":1958,"title":"1958年，人民英雄纪念碑揭幕。","event":"1958年，人民英雄纪念碑揭幕。","day":1},{"date":"19600501","month":5,"subTitle":"苏联击落美国U-2型高空侦察机。","year":1960,"title":"1960年，苏联击落美国U-2型高空侦察机。","event":"1960年，苏联击落美国U-2型高空侦察机。","day":1},{"date":"19610501","month":5,"subTitle":"卡斯特罗废除选举制。","year":1961,"title":"1961年，卡斯特罗废除选举制。","event":"1961年，卡斯特罗废除选举制。","day":1},{"date":"19630501","month":5,"subTitle":"我国第一艘远洋货轮\u201c跃进号\u201d沉没。","year":1963,"title":"1963年，我国第一艘远洋货轮\u201c跃进号\u201d沉没。","event":"1963年，我国第一艘远洋货轮\u201c跃进号\u201d沉没。","day":1},{"date":"19670501","month":5,"subTitle":"\u201c猫王\u201d普雷斯利举行结婚典礼。","year":1967,"title":"1967年，\u201c猫王\u201d普雷斯利举行结婚典礼。","event":"1967年，\u201c猫王\u201d普雷斯利举行结婚典礼。","day":1},{"date":"19770501","month":5,"subTitle":"《人民日报》发表华国锋的文章。","year":1977,"title":"1977年，《人民日报》发表华国锋的文章。","event":"1977年，《人民日报》发表华国锋的文章。","day":1},{"date":"19780501","month":5,"subTitle":"北京电视台更名为中央电视台英文简称CCTV。","year":1978,"title":"1978年，北京电视台更名为中央电视台，英文简称CCTV。","event":"1978年，北京电视台更名为中央电视台，英文简称CCTV。","day":1}],"content":"历史上的1773年5月1日，清朝开设《四库全书》编纂馆。"},"name":"default","title":"历史上的5月1日","type":"text"},"intentName":"查询历史事件","runSequence":"nlgFirst","nlg":"历史上的1773年5月1日，清朝开设《四库全书》编纂馆。","intentId":"5d8ae7040d32eb000d70f89e","speak":{"text":"历史上的1773年5月1日，清朝开设《四库全书》编纂馆。","type":"text"},"command":{"api":""},"taskId":"5d8ae7040d32eb000d70f883","status":1}
     * contextId : 21db7278798949a0877b003ac4c05455
     * sessionId : 21db7278798949a0877b003ac4c05455
     */

    private String recordId;
    private String skillId;
    private String requestId;
    private NluBean nlu;
    private DmBean dm;
    private String contextId;
    private String sessionId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public NluBean getNlu() {
        return nlu;
    }

    public void setNlu(NluBean nlu) {
        this.nlu = nlu;
    }

    public DmBean getDm() {
        return dm;
    }

    public void setDm(DmBean dm) {
        this.dm = dm;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public static class NluBean {
        /**
         * skillId : 2019031800001161
         * res : 5d8ae7040d32eb000d70f883
         * input : 历史上五月一号有什么大事发生
         * inittime : 34.682861328125
         * loadtime : 68.33203125
         * skill : 日历
         * skillVersion : 38
         * source : dui
         * systime : 341.64721679687
         * semantics : {"request":{"slots":[{"pos":[3,6],"rawvalue":"五月一号","name":"阳历日期","rawpinyin":"wu yue yi hao","value":"20190501"},{"name":"intent","value":"查询历史事件"}],"task":"日历","confidence":1,"slotcount":2,"rules":{"5d8ae7040d32eb000d70fac1":{"阳历日期":["20190501",3,6],"intent":["查询历史事件",-1,-1]}}}}
         * version : 2019.1.15.20:40:58
         * timestamp : 1570692358
         */

        private String skillId;
        private String res;
        private String input;
        private double inittime;
        private double loadtime;
        private String skill;
        private String skillVersion;
        private String source;
        private double systime;
        private SemanticsBean semantics;
        private String version;
        private int timestamp;

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getRes() {
            return res;
        }

        public void setRes(String res) {
            this.res = res;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public double getInittime() {
            return inittime;
        }

        public void setInittime(double inittime) {
            this.inittime = inittime;
        }

        public double getLoadtime() {
            return loadtime;
        }

        public void setLoadtime(double loadtime) {
            this.loadtime = loadtime;
        }

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public String getSkillVersion() {
            return skillVersion;
        }

        public void setSkillVersion(String skillVersion) {
            this.skillVersion = skillVersion;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public double getSystime() {
            return systime;
        }

        public void setSystime(double systime) {
            this.systime = systime;
        }

        public SemanticsBean getSemantics() {
            return semantics;
        }

        public void setSemantics(SemanticsBean semantics) {
            this.semantics = semantics;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public static class SemanticsBean {
            /**
             * request : {"slots":[{"pos":[3,6],"rawvalue":"五月一号","name":"阳历日期","rawpinyin":"wu yue yi hao","value":"20190501"},{"name":"intent","value":"查询历史事件"}],"task":"日历","confidence":1,"slotcount":2,"rules":{"5d8ae7040d32eb000d70fac1":{"阳历日期":["20190501",3,6],"intent":["查询历史事件",-1,-1]}}}
             */

            private RequestBean request;

            public RequestBean getRequest() {
                return request;
            }

            public void setRequest(RequestBean request) {
                this.request = request;
            }

            public static class RequestBean {
                /**
                 * slots : [{"pos":[3,6],"rawvalue":"五月一号","name":"阳历日期","rawpinyin":"wu yue yi hao","value":"20190501"},{"name":"intent","value":"查询历史事件"}]
                 * task : 日历
                 * confidence : 1
                 * slotcount : 2
                 * rules : {"5d8ae7040d32eb000d70fac1":{"阳历日期":["20190501",3,6],"intent":["查询历史事件",-1,-1]}}
                 */

                private String task;
                private int confidence;
                private int slotcount;
                private RulesBean rules;
                private List<SlotsBean> slots;

                public String getTask() {
                    return task;
                }

                public void setTask(String task) {
                    this.task = task;
                }

                public int getConfidence() {
                    return confidence;
                }

                public void setConfidence(int confidence) {
                    this.confidence = confidence;
                }

                public int getSlotcount() {
                    return slotcount;
                }

                public void setSlotcount(int slotcount) {
                    this.slotcount = slotcount;
                }

                public RulesBean getRules() {
                    return rules;
                }

                public void setRules(RulesBean rules) {
                    this.rules = rules;
                }

                public List<SlotsBean> getSlots() {
                    return slots;
                }

                public void setSlots(List<SlotsBean> slots) {
                    this.slots = slots;
                }

                public static class RulesBean {
                    /**
                     * 5d8ae7040d32eb000d70fac1 : {"阳历日期":["20190501",3,6],"intent":["查询历史事件",-1,-1]}
                     */

                    @SerializedName("5d8ae7040d32eb000d70fac1")
                    private _$5d8ae7040d32eb000d70fac1Bean _$5d8ae7040d32eb000d70fac1;

                    public _$5d8ae7040d32eb000d70fac1Bean get_$5d8ae7040d32eb000d70fac1() {
                        return _$5d8ae7040d32eb000d70fac1;
                    }

                    public void set_$5d8ae7040d32eb000d70fac1(_$5d8ae7040d32eb000d70fac1Bean _$5d8ae7040d32eb000d70fac1) {
                        this._$5d8ae7040d32eb000d70fac1 = _$5d8ae7040d32eb000d70fac1;
                    }

                    public static class _$5d8ae7040d32eb000d70fac1Bean {
                        private List<String> 阳历日期;
                        private List<String> intent;

                        public List<String> get阳历日期() {
                            return 阳历日期;
                        }

                        public void set阳历日期(List<String> 阳历日期) {
                            this.阳历日期 = 阳历日期;
                        }

                        public List<String> getIntent() {
                            return intent;
                        }

                        public void setIntent(List<String> intent) {
                            this.intent = intent;
                        }
                    }
                }

                public static class SlotsBean {
                    /**
                     * pos : [3,6]
                     * rawvalue : 五月一号
                     * name : 阳历日期
                     * rawpinyin : wu yue yi hao
                     * value : 20190501
                     */

                    private String rawvalue;
                    private String name;
                    private String rawpinyin;
                    private String value;
                    private List<Integer> pos;

                    public String getRawvalue() {
                        return rawvalue;
                    }

                    public void setRawvalue(String rawvalue) {
                        this.rawvalue = rawvalue;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getRawpinyin() {
                        return rawpinyin;
                    }

                    public void setRawpinyin(String rawpinyin) {
                        this.rawpinyin = rawpinyin;
                    }

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                    public List<Integer> getPos() {
                        return pos;
                    }

                    public void setPos(List<Integer> pos) {
                        this.pos = pos;
                    }
                }
            }
        }
    }

    public static class DmBean {
        /**
         * input : 历史上五月一号有什么大事发生
         * shouldEndSession : true
         * task : 日历
         * widget : {"widgetName":"default","duiWidget":"text","subTitle":"历史上的1773年5月1日，清朝开设《四库全书》编纂馆。<br>","extra":{"result":[{"date":"17070501","month":5,"subTitle":"英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","year":1707,"title":"1707年，英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","event":"1707年，英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","day":1},{"date":"17730501","month":5,"subTitle":"清朝开设《四库全书》编纂馆。","year":1773,"title":"1773年，清朝开设《四库全书》编纂馆。","event":"1773年，清朝开设《四库全书》编纂馆。","day":1},{"date":"17900501","month":5,"subTitle":"美国完成第一次人口普查。","year":1790,"title":"1790年，美国完成第一次人口普查。","event":"1790年，美国完成第一次人口普查。","day":1},{"date":"18340501","month":5,"subTitle":"奴隶制在英国领土范围内被废除。","year":1834,"title":"1834年，奴隶制在英国领土范围内被废除。","event":"1834年，奴隶制在英国领土范围内被废除。","day":1},{"date":"18400501","month":5,"subTitle":"英国正式发行世界上首枚邮票黑便士正面为维多利亚女王的头像。","year":1840,"title":"1840年，英国正式发行世界上首枚邮票黑便士，正面为维多利亚女王的头像。","event":"1840年，英国正式发行世界上首枚邮票黑便士，正面为维多利亚女王的头像。","day":1},{"date":"18510501","month":5,"subTitle":"第一届国际博览会在英国伦敦开幕。","year":1851,"title":"1851年，第一届国际博览会在英国伦敦开幕。","event":"1851年，第一届国际博览会在英国伦敦开幕。","day":1},{"date":"18860501","month":5,"subTitle":"美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱要求八小时工作制。","year":1886,"title":"1886年，美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱，要求八小时工作制。","event":"1886年，美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱，要求八小时工作制。","day":1},{"date":"18940501","month":5,"subTitle":"甲午战争：日本宣布对中国开战。","year":1894,"title":"1894年，甲午战争：日本宣布对中国开战。","event":"1894年，甲午战争：日本宣布对中国开战。","day":1},{"date":"18980501","month":5,"subTitle":"美西战争：美国海军占领马尼拉。","year":1898,"title":"1898年，美西战争：美国海军占领马尼拉。","event":"1898年，美西战争：美国海军占领马尼拉。","day":1},{"date":"19140501","month":5,"subTitle":"袁世凯颁布《中华民国约法》。","year":1914,"title":"1914年，袁世凯颁布《中华民国约法》。","event":"1914年，袁世凯颁布《中华民国约法》。","day":1},{"date":"19170501","month":5,"subTitle":"督军团欲迫政府对德宣战。","year":1917,"title":"1917年，督军团欲迫政府对德宣战。","event":"1917年，督军团欲迫政府对德宣战。","day":1},{"date":"19220501","month":5,"subTitle":"第一次全国劳动大会召开。","year":1922,"title":"1922年，第一次全国劳动大会召开。","event":"1922年，第一次全国劳动大会召开。","day":1},{"date":"19260501","month":5,"subTitle":"英国大罢工爆发。","year":1926,"title":"1926年，英国大罢工爆发。","event":"1926年，英国大罢工爆发。","day":1},{"date":"19270501","month":5,"subTitle":"广东海丰、陆丰农民起义。","year":1927,"title":"1927年，广东海丰、陆丰农民起义。","event":"1927年，广东海丰、陆丰农民起义。","day":1},{"date":"19310501","month":5,"subTitle":"当时世界上最高的建筑纽约帝国大厦剪彩。","year":1931,"title":"1931年，当时世界上最高的建筑纽约帝国大厦剪彩。","event":"1931年，当时世界上最高的建筑纽约帝国大厦剪彩。","day":1},{"date":"19330501","month":5,"subTitle":"国民党部署第五次对苏区的\u201c围剿\u201d。","year":1933,"title":"1933年，国民党部署第五次对苏区的\u201c围剿\u201d。","event":"1933年，国民党部署第五次对苏区的\u201c围剿\u201d。","day":1},{"date":"19350501","month":5,"subTitle":"红军抢渡金沙江。","year":1935,"title":"1935年，红军抢渡金沙江。","event":"1935年，红军抢渡金沙江。","day":1},{"date":"19390501","month":5,"subTitle":"中国军队反攻南昌失利。","year":1939,"title":"1939年，中国军队反攻南昌失利。","event":"1939年，中国军队反攻南昌失利。","day":1},{"date":"19410501","month":5,"subTitle":"美国《公民凯恩》影片上映。","year":1941,"title":"1941年，美国《公民凯恩》影片上映。","event":"1941年，美国《公民凯恩》影片上映。","day":1},{"date":"19480501","month":5,"subTitle":"毛泽东提议召开新政协会议得到各界响应。","year":1948,"title":"1948年，毛泽东提议召开新政协会议得到各界响应。","event":"1948年，毛泽东提议召开新政协会议得到各界响应。","day":1},{"date":"19500501","month":5,"subTitle":"解放军解放海南岛。","year":1950,"title":"1950年，解放军解放海南岛。","event":"1950年，解放军解放海南岛。","day":1},{"date":"19500501","month":5,"subTitle":"中央号召开展大规模整风运动。","year":1950,"title":"1950年，中央号召开展大规模整风运动。","event":"1950年，中央号召开展大规模整风运动。","day":1},{"date":"19570501","month":5,"subTitle":"戚烈云破游泳世界纪录。","year":1957,"title":"1957年，戚烈云破游泳世界纪录。","event":"1957年，戚烈云破游泳世界纪录。","day":1},{"date":"19580501","month":5,"subTitle":"人民英雄纪念碑揭幕。","year":1958,"title":"1958年，人民英雄纪念碑揭幕。","event":"1958年，人民英雄纪念碑揭幕。","day":1},{"date":"19600501","month":5,"subTitle":"苏联击落美国U-2型高空侦察机。","year":1960,"title":"1960年，苏联击落美国U-2型高空侦察机。","event":"1960年，苏联击落美国U-2型高空侦察机。","day":1},{"date":"19610501","month":5,"subTitle":"卡斯特罗废除选举制。","year":1961,"title":"1961年，卡斯特罗废除选举制。","event":"1961年，卡斯特罗废除选举制。","day":1},{"date":"19630501","month":5,"subTitle":"我国第一艘远洋货轮\u201c跃进号\u201d沉没。","year":1963,"title":"1963年，我国第一艘远洋货轮\u201c跃进号\u201d沉没。","event":"1963年，我国第一艘远洋货轮\u201c跃进号\u201d沉没。","day":1},{"date":"19670501","month":5,"subTitle":"\u201c猫王\u201d普雷斯利举行结婚典礼。","year":1967,"title":"1967年，\u201c猫王\u201d普雷斯利举行结婚典礼。","event":"1967年，\u201c猫王\u201d普雷斯利举行结婚典礼。","day":1},{"date":"19770501","month":5,"subTitle":"《人民日报》发表华国锋的文章。","year":1977,"title":"1977年，《人民日报》发表华国锋的文章。","event":"1977年，《人民日报》发表华国锋的文章。","day":1},{"date":"19780501","month":5,"subTitle":"北京电视台更名为中央电视台英文简称CCTV。","year":1978,"title":"1978年，北京电视台更名为中央电视台，英文简称CCTV。","event":"1978年，北京电视台更名为中央电视台，英文简称CCTV。","day":1}],"content":"历史上的1773年5月1日，清朝开设《四库全书》编纂馆。"},"name":"default","title":"历史上的5月1日","type":"text"}
         * intentName : 查询历史事件
         * runSequence : nlgFirst
         * nlg : 历史上的1773年5月1日，清朝开设《四库全书》编纂馆。
         * intentId : 5d8ae7040d32eb000d70f89e
         * speak : {"text":"历史上的1773年5月1日，清朝开设《四库全书》编纂馆。","type":"text"}
         * command : {"api":""}
         * taskId : 5d8ae7040d32eb000d70f883
         * status : 1
         */

        private String input;
        private boolean shouldEndSession;
        private String task;
        private WidgetBean widget;
        private String intentName;
        private String runSequence;
        private String nlg;
        private String intentId;
        private SpeakBean speak;
        private CommandBean command;
        private String taskId;
        private int status;

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public boolean isShouldEndSession() {
            return shouldEndSession;
        }

        public void setShouldEndSession(boolean shouldEndSession) {
            this.shouldEndSession = shouldEndSession;
        }

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }

        public WidgetBean getWidget() {
            return widget;
        }

        public void setWidget(WidgetBean widget) {
            this.widget = widget;
        }

        public String getIntentName() {
            return intentName;
        }

        public void setIntentName(String intentName) {
            this.intentName = intentName;
        }

        public String getRunSequence() {
            return runSequence;
        }

        public void setRunSequence(String runSequence) {
            this.runSequence = runSequence;
        }

        public String getNlg() {
            return nlg;
        }

        public void setNlg(String nlg) {
            this.nlg = nlg;
        }

        public String getIntentId() {
            return intentId;
        }

        public void setIntentId(String intentId) {
            this.intentId = intentId;
        }

        public SpeakBean getSpeak() {
            return speak;
        }

        public void setSpeak(SpeakBean speak) {
            this.speak = speak;
        }

        public CommandBean getCommand() {
            return command;
        }

        public void setCommand(CommandBean command) {
            this.command = command;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class WidgetBean {
            /**
             * widgetName : default
             * duiWidget : text
             * subTitle : 历史上的1773年5月1日，清朝开设《四库全书》编纂馆。<br>
             * extra : {"result":[{"date":"17070501","month":5,"subTitle":"英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","year":1707,"title":"1707年，英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","event":"1707年，英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","day":1},{"date":"17730501","month":5,"subTitle":"清朝开设《四库全书》编纂馆。","year":1773,"title":"1773年，清朝开设《四库全书》编纂馆。","event":"1773年，清朝开设《四库全书》编纂馆。","day":1},{"date":"17900501","month":5,"subTitle":"美国完成第一次人口普查。","year":1790,"title":"1790年，美国完成第一次人口普查。","event":"1790年，美国完成第一次人口普查。","day":1},{"date":"18340501","month":5,"subTitle":"奴隶制在英国领土范围内被废除。","year":1834,"title":"1834年，奴隶制在英国领土范围内被废除。","event":"1834年，奴隶制在英国领土范围内被废除。","day":1},{"date":"18400501","month":5,"subTitle":"英国正式发行世界上首枚邮票黑便士正面为维多利亚女王的头像。","year":1840,"title":"1840年，英国正式发行世界上首枚邮票黑便士，正面为维多利亚女王的头像。","event":"1840年，英国正式发行世界上首枚邮票黑便士，正面为维多利亚女王的头像。","day":1},{"date":"18510501","month":5,"subTitle":"第一届国际博览会在英国伦敦开幕。","year":1851,"title":"1851年，第一届国际博览会在英国伦敦开幕。","event":"1851年，第一届国际博览会在英国伦敦开幕。","day":1},{"date":"18860501","month":5,"subTitle":"美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱要求八小时工作制。","year":1886,"title":"1886年，美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱，要求八小时工作制。","event":"1886年，美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱，要求八小时工作制。","day":1},{"date":"18940501","month":5,"subTitle":"甲午战争：日本宣布对中国开战。","year":1894,"title":"1894年，甲午战争：日本宣布对中国开战。","event":"1894年，甲午战争：日本宣布对中国开战。","day":1},{"date":"18980501","month":5,"subTitle":"美西战争：美国海军占领马尼拉。","year":1898,"title":"1898年，美西战争：美国海军占领马尼拉。","event":"1898年，美西战争：美国海军占领马尼拉。","day":1},{"date":"19140501","month":5,"subTitle":"袁世凯颁布《中华民国约法》。","year":1914,"title":"1914年，袁世凯颁布《中华民国约法》。","event":"1914年，袁世凯颁布《中华民国约法》。","day":1},{"date":"19170501","month":5,"subTitle":"督军团欲迫政府对德宣战。","year":1917,"title":"1917年，督军团欲迫政府对德宣战。","event":"1917年，督军团欲迫政府对德宣战。","day":1},{"date":"19220501","month":5,"subTitle":"第一次全国劳动大会召开。","year":1922,"title":"1922年，第一次全国劳动大会召开。","event":"1922年，第一次全国劳动大会召开。","day":1},{"date":"19260501","month":5,"subTitle":"英国大罢工爆发。","year":1926,"title":"1926年，英国大罢工爆发。","event":"1926年，英国大罢工爆发。","day":1},{"date":"19270501","month":5,"subTitle":"广东海丰、陆丰农民起义。","year":1927,"title":"1927年，广东海丰、陆丰农民起义。","event":"1927年，广东海丰、陆丰农民起义。","day":1},{"date":"19310501","month":5,"subTitle":"当时世界上最高的建筑纽约帝国大厦剪彩。","year":1931,"title":"1931年，当时世界上最高的建筑纽约帝国大厦剪彩。","event":"1931年，当时世界上最高的建筑纽约帝国大厦剪彩。","day":1},{"date":"19330501","month":5,"subTitle":"国民党部署第五次对苏区的\u201c围剿\u201d。","year":1933,"title":"1933年，国民党部署第五次对苏区的\u201c围剿\u201d。","event":"1933年，国民党部署第五次对苏区的\u201c围剿\u201d。","day":1},{"date":"19350501","month":5,"subTitle":"红军抢渡金沙江。","year":1935,"title":"1935年，红军抢渡金沙江。","event":"1935年，红军抢渡金沙江。","day":1},{"date":"19390501","month":5,"subTitle":"中国军队反攻南昌失利。","year":1939,"title":"1939年，中国军队反攻南昌失利。","event":"1939年，中国军队反攻南昌失利。","day":1},{"date":"19410501","month":5,"subTitle":"美国《公民凯恩》影片上映。","year":1941,"title":"1941年，美国《公民凯恩》影片上映。","event":"1941年，美国《公民凯恩》影片上映。","day":1},{"date":"19480501","month":5,"subTitle":"毛泽东提议召开新政协会议得到各界响应。","year":1948,"title":"1948年，毛泽东提议召开新政协会议得到各界响应。","event":"1948年，毛泽东提议召开新政协会议得到各界响应。","day":1},{"date":"19500501","month":5,"subTitle":"解放军解放海南岛。","year":1950,"title":"1950年，解放军解放海南岛。","event":"1950年，解放军解放海南岛。","day":1},{"date":"19500501","month":5,"subTitle":"中央号召开展大规模整风运动。","year":1950,"title":"1950年，中央号召开展大规模整风运动。","event":"1950年，中央号召开展大规模整风运动。","day":1},{"date":"19570501","month":5,"subTitle":"戚烈云破游泳世界纪录。","year":1957,"title":"1957年，戚烈云破游泳世界纪录。","event":"1957年，戚烈云破游泳世界纪录。","day":1},{"date":"19580501","month":5,"subTitle":"人民英雄纪念碑揭幕。","year":1958,"title":"1958年，人民英雄纪念碑揭幕。","event":"1958年，人民英雄纪念碑揭幕。","day":1},{"date":"19600501","month":5,"subTitle":"苏联击落美国U-2型高空侦察机。","year":1960,"title":"1960年，苏联击落美国U-2型高空侦察机。","event":"1960年，苏联击落美国U-2型高空侦察机。","day":1},{"date":"19610501","month":5,"subTitle":"卡斯特罗废除选举制。","year":1961,"title":"1961年，卡斯特罗废除选举制。","event":"1961年，卡斯特罗废除选举制。","day":1},{"date":"19630501","month":5,"subTitle":"我国第一艘远洋货轮\u201c跃进号\u201d沉没。","year":1963,"title":"1963年，我国第一艘远洋货轮\u201c跃进号\u201d沉没。","event":"1963年，我国第一艘远洋货轮\u201c跃进号\u201d沉没。","day":1},{"date":"19670501","month":5,"subTitle":"\u201c猫王\u201d普雷斯利举行结婚典礼。","year":1967,"title":"1967年，\u201c猫王\u201d普雷斯利举行结婚典礼。","event":"1967年，\u201c猫王\u201d普雷斯利举行结婚典礼。","day":1},{"date":"19770501","month":5,"subTitle":"《人民日报》发表华国锋的文章。","year":1977,"title":"1977年，《人民日报》发表华国锋的文章。","event":"1977年，《人民日报》发表华国锋的文章。","day":1},{"date":"19780501","month":5,"subTitle":"北京电视台更名为中央电视台英文简称CCTV。","year":1978,"title":"1978年，北京电视台更名为中央电视台，英文简称CCTV。","event":"1978年，北京电视台更名为中央电视台，英文简称CCTV。","day":1}],"content":"历史上的1773年5月1日，清朝开设《四库全书》编纂馆。"}
             * name : default
             * title : 历史上的5月1日
             * type : text
             */

            private String widgetName;
            private String duiWidget;
            private String subTitle;
            private ExtraBean extra;
            private String name;
            private String title;
            private String type;

            public String getWidgetName() {
                return widgetName;
            }

            public void setWidgetName(String widgetName) {
                this.widgetName = widgetName;
            }

            public String getDuiWidget() {
                return duiWidget;
            }

            public void setDuiWidget(String duiWidget) {
                this.duiWidget = duiWidget;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public ExtraBean getExtra() {
                return extra;
            }

            public void setExtra(ExtraBean extra) {
                this.extra = extra;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public static class ExtraBean {
                /**
                 * result : [{"date":"17070501","month":5,"subTitle":"英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","year":1707,"title":"1707年，英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","event":"1707年，英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。","day":1},{"date":"17730501","month":5,"subTitle":"清朝开设《四库全书》编纂馆。","year":1773,"title":"1773年，清朝开设《四库全书》编纂馆。","event":"1773年，清朝开设《四库全书》编纂馆。","day":1},{"date":"17900501","month":5,"subTitle":"美国完成第一次人口普查。","year":1790,"title":"1790年，美国完成第一次人口普查。","event":"1790年，美国完成第一次人口普查。","day":1},{"date":"18340501","month":5,"subTitle":"奴隶制在英国领土范围内被废除。","year":1834,"title":"1834年，奴隶制在英国领土范围内被废除。","event":"1834年，奴隶制在英国领土范围内被废除。","day":1},{"date":"18400501","month":5,"subTitle":"英国正式发行世界上首枚邮票黑便士正面为维多利亚女王的头像。","year":1840,"title":"1840年，英国正式发行世界上首枚邮票黑便士，正面为维多利亚女王的头像。","event":"1840年，英国正式发行世界上首枚邮票黑便士，正面为维多利亚女王的头像。","day":1},{"date":"18510501","month":5,"subTitle":"第一届国际博览会在英国伦敦开幕。","year":1851,"title":"1851年，第一届国际博览会在英国伦敦开幕。","event":"1851年，第一届国际博览会在英国伦敦开幕。","day":1},{"date":"18860501","month":5,"subTitle":"美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱要求八小时工作制。","year":1886,"title":"1886年，美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱，要求八小时工作制。","event":"1886年，美国芝加哥发生大规模游行示威\u2014\u2014干草市场暴乱，要求八小时工作制。","day":1},{"date":"18940501","month":5,"subTitle":"甲午战争：日本宣布对中国开战。","year":1894,"title":"1894年，甲午战争：日本宣布对中国开战。","event":"1894年，甲午战争：日本宣布对中国开战。","day":1},{"date":"18980501","month":5,"subTitle":"美西战争：美国海军占领马尼拉。","year":1898,"title":"1898年，美西战争：美国海军占领马尼拉。","event":"1898年，美西战争：美国海军占领马尼拉。","day":1},{"date":"19140501","month":5,"subTitle":"袁世凯颁布《中华民国约法》。","year":1914,"title":"1914年，袁世凯颁布《中华民国约法》。","event":"1914年，袁世凯颁布《中华民国约法》。","day":1},{"date":"19170501","month":5,"subTitle":"督军团欲迫政府对德宣战。","year":1917,"title":"1917年，督军团欲迫政府对德宣战。","event":"1917年，督军团欲迫政府对德宣战。","day":1},{"date":"19220501","month":5,"subTitle":"第一次全国劳动大会召开。","year":1922,"title":"1922年，第一次全国劳动大会召开。","event":"1922年，第一次全国劳动大会召开。","day":1},{"date":"19260501","month":5,"subTitle":"英国大罢工爆发。","year":1926,"title":"1926年，英国大罢工爆发。","event":"1926年，英国大罢工爆发。","day":1},{"date":"19270501","month":5,"subTitle":"广东海丰、陆丰农民起义。","year":1927,"title":"1927年，广东海丰、陆丰农民起义。","event":"1927年，广东海丰、陆丰农民起义。","day":1},{"date":"19310501","month":5,"subTitle":"当时世界上最高的建筑纽约帝国大厦剪彩。","year":1931,"title":"1931年，当时世界上最高的建筑纽约帝国大厦剪彩。","event":"1931年，当时世界上最高的建筑纽约帝国大厦剪彩。","day":1},{"date":"19330501","month":5,"subTitle":"国民党部署第五次对苏区的\u201c围剿\u201d。","year":1933,"title":"1933年，国民党部署第五次对苏区的\u201c围剿\u201d。","event":"1933年，国民党部署第五次对苏区的\u201c围剿\u201d。","day":1},{"date":"19350501","month":5,"subTitle":"红军抢渡金沙江。","year":1935,"title":"1935年，红军抢渡金沙江。","event":"1935年，红军抢渡金沙江。","day":1},{"date":"19390501","month":5,"subTitle":"中国军队反攻南昌失利。","year":1939,"title":"1939年，中国军队反攻南昌失利。","event":"1939年，中国军队反攻南昌失利。","day":1},{"date":"19410501","month":5,"subTitle":"美国《公民凯恩》影片上映。","year":1941,"title":"1941年，美国《公民凯恩》影片上映。","event":"1941年，美国《公民凯恩》影片上映。","day":1},{"date":"19480501","month":5,"subTitle":"毛泽东提议召开新政协会议得到各界响应。","year":1948,"title":"1948年，毛泽东提议召开新政协会议得到各界响应。","event":"1948年，毛泽东提议召开新政协会议得到各界响应。","day":1},{"date":"19500501","month":5,"subTitle":"解放军解放海南岛。","year":1950,"title":"1950年，解放军解放海南岛。","event":"1950年，解放军解放海南岛。","day":1},{"date":"19500501","month":5,"subTitle":"中央号召开展大规模整风运动。","year":1950,"title":"1950年，中央号召开展大规模整风运动。","event":"1950年，中央号召开展大规模整风运动。","day":1},{"date":"19570501","month":5,"subTitle":"戚烈云破游泳世界纪录。","year":1957,"title":"1957年，戚烈云破游泳世界纪录。","event":"1957年，戚烈云破游泳世界纪录。","day":1},{"date":"19580501","month":5,"subTitle":"人民英雄纪念碑揭幕。","year":1958,"title":"1958年，人民英雄纪念碑揭幕。","event":"1958年，人民英雄纪念碑揭幕。","day":1},{"date":"19600501","month":5,"subTitle":"苏联击落美国U-2型高空侦察机。","year":1960,"title":"1960年，苏联击落美国U-2型高空侦察机。","event":"1960年，苏联击落美国U-2型高空侦察机。","day":1},{"date":"19610501","month":5,"subTitle":"卡斯特罗废除选举制。","year":1961,"title":"1961年，卡斯特罗废除选举制。","event":"1961年，卡斯特罗废除选举制。","day":1},{"date":"19630501","month":5,"subTitle":"我国第一艘远洋货轮\u201c跃进号\u201d沉没。","year":1963,"title":"1963年，我国第一艘远洋货轮\u201c跃进号\u201d沉没。","event":"1963年，我国第一艘远洋货轮\u201c跃进号\u201d沉没。","day":1},{"date":"19670501","month":5,"subTitle":"\u201c猫王\u201d普雷斯利举行结婚典礼。","year":1967,"title":"1967年，\u201c猫王\u201d普雷斯利举行结婚典礼。","event":"1967年，\u201c猫王\u201d普雷斯利举行结婚典礼。","day":1},{"date":"19770501","month":5,"subTitle":"《人民日报》发表华国锋的文章。","year":1977,"title":"1977年，《人民日报》发表华国锋的文章。","event":"1977年，《人民日报》发表华国锋的文章。","day":1},{"date":"19780501","month":5,"subTitle":"北京电视台更名为中央电视台英文简称CCTV。","year":1978,"title":"1978年，北京电视台更名为中央电视台，英文简称CCTV。","event":"1978年，北京电视台更名为中央电视台，英文简称CCTV。","day":1}]
                 * content : 历史上的1773年5月1日，清朝开设《四库全书》编纂馆。
                 */

                private String content;
                private List<ResultBean> result;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public List<ResultBean> getResult() {
                    return result;
                }

                public void setResult(List<ResultBean> result) {
                    this.result = result;
                }

                public static class ResultBean {
                    /**
                     * date : 17070501
                     * month : 5
                     * subTitle : 英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。
                     * year : 1707
                     * title : 1707年，英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。
                     * event : 1707年，英格兰（包含威尔士）和苏格兰在大不列颠王国的名称下实行联合。
                     * day : 1
                     */

                    private String date;
                    private int month;
                    private String subTitle;
                    private int year;
                    private String title;
                    private String event;
                    private int day;

                    public String getDate() {
                        return date;
                    }

                    public void setDate(String date) {
                        this.date = date;
                    }

                    public int getMonth() {
                        return month;
                    }

                    public void setMonth(int month) {
                        this.month = month;
                    }

                    public String getSubTitle() {
                        return subTitle;
                    }

                    public void setSubTitle(String subTitle) {
                        this.subTitle = subTitle;
                    }

                    public int getYear() {
                        return year;
                    }

                    public void setYear(int year) {
                        this.year = year;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getEvent() {
                        return event;
                    }

                    public void setEvent(String event) {
                        this.event = event;
                    }

                    public int getDay() {
                        return day;
                    }

                    public void setDay(int day) {
                        this.day = day;
                    }
                }
            }
        }

        public static class SpeakBean {
            /**
             * text : 历史上的1773年5月1日，清朝开设《四库全书》编纂馆。
             * type : text
             */

            private String text;
            private String type;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class CommandBean {
            /**
             * api :
             */

            private String api;

            public String getApi() {
                return api;
            }

            public void setApi(String api) {
                this.api = api;
            }
        }
    }
}
