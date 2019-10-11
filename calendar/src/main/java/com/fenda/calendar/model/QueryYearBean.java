package com.fenda.calendar.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/10/10
 * @Describe: 查询年份实体
 */
public class QueryYearBean {

    /**
     * recordId : 63c615d39c5c4731a5d776217a6cb2a6
     * skillId : 2019031800001161
     * requestId : 63c615d39c5c4731a5d776217a6cb2a6
     * nlu : {"skillId":"2019031800001161","res":"5d8ae7040d32eb000d70f883","input":"今年是哪一年","inittime":41.783203125,"loadtime":44.832763671875,"skill":"日历","skillVersion":"38","source":"dui","systime":138.80883789062,"semantics":{"request":{"slots":[{"pos":[0,1],"rawvalue":"今年","name":"年","rawpinyin":"jin nian","value":"2019"},{"name":"intent","value":"查询年份"}],"task":"日历","confidence":1,"slotcount":2,"rules":{"5d8ae7040d32eb000d70fb28":{"年":["2019",0,1],"intent":["查询年份",-1,-1]}}}},"version":"2019.1.15.20:40:58","timestamp":1570692491}
     * dm : {"input":"今年是哪一年,","shouldEndSession":true,"task":"日历","widget":{"widgetName":"default","duiWidget":"text","response":{"nlyear":"己亥","year":2019,"zodiac":"猪","after":{"year":2079}},"name":"default","text":2019,"type":"text"},"intentName":"查询年份","runSequence":"nlgFirst","nlg":"今年是2019年","intentId":"5d8ae7040d32eb000d70f8a0","speak":{"text":"今年是2019年","type":"text"},"command":{"api":""},"taskId":"5d8ae7040d32eb000d70f883","status":1}
     * contextId : 85963a1935fd41fa81079d3c1e0a7079
     * sessionId : 85963a1935fd41fa81079d3c1e0a7079
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
         * input : 今年是哪一年
         * inittime : 41.783203125
         * loadtime : 44.832763671875
         * skill : 日历
         * skillVersion : 38
         * source : dui
         * systime : 138.80883789062
         * semantics : {"request":{"slots":[{"pos":[0,1],"rawvalue":"今年","name":"年","rawpinyin":"jin nian","value":"2019"},{"name":"intent","value":"查询年份"}],"task":"日历","confidence":1,"slotcount":2,"rules":{"5d8ae7040d32eb000d70fb28":{"年":["2019",0,1],"intent":["查询年份",-1,-1]}}}}
         * version : 2019.1.15.20:40:58
         * timestamp : 1570692491
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
             * request : {"slots":[{"pos":[0,1],"rawvalue":"今年","name":"年","rawpinyin":"jin nian","value":"2019"},{"name":"intent","value":"查询年份"}],"task":"日历","confidence":1,"slotcount":2,"rules":{"5d8ae7040d32eb000d70fb28":{"年":["2019",0,1],"intent":["查询年份",-1,-1]}}}
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
                 * slots : [{"pos":[0,1],"rawvalue":"今年","name":"年","rawpinyin":"jin nian","value":"2019"},{"name":"intent","value":"查询年份"}]
                 * task : 日历
                 * confidence : 1
                 * slotcount : 2
                 * rules : {"5d8ae7040d32eb000d70fb28":{"年":["2019",0,1],"intent":["查询年份",-1,-1]}}
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
                     * 5d8ae7040d32eb000d70fb28 : {"年":["2019",0,1],"intent":["查询年份",-1,-1]}
                     */

                    @SerializedName("5d8ae7040d32eb000d70fb28")
                    private _$5d8ae7040d32eb000d70fb28Bean _$5d8ae7040d32eb000d70fb28;

                    public _$5d8ae7040d32eb000d70fb28Bean get_$5d8ae7040d32eb000d70fb28() {
                        return _$5d8ae7040d32eb000d70fb28;
                    }

                    public void set_$5d8ae7040d32eb000d70fb28(_$5d8ae7040d32eb000d70fb28Bean _$5d8ae7040d32eb000d70fb28) {
                        this._$5d8ae7040d32eb000d70fb28 = _$5d8ae7040d32eb000d70fb28;
                    }

                    public static class _$5d8ae7040d32eb000d70fb28Bean {
                        private List<String> 年;
                        private List<String> intent;

                        public List<String> get年() {
                            return 年;
                        }

                        public void set年(List<String> 年) {
                            this.年 = 年;
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
                     * pos : [0,1]
                     * rawvalue : 今年
                     * name : 年
                     * rawpinyin : jin nian
                     * value : 2019
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
         * input : 今年是哪一年,
         * shouldEndSession : true
         * task : 日历
         * widget : {"widgetName":"default","duiWidget":"text","response":{"nlyear":"己亥","year":2019,"zodiac":"猪","after":{"year":2079}},"name":"default","text":2019,"type":"text"}
         * intentName : 查询年份
         * runSequence : nlgFirst
         * nlg : 今年是2019年
         * intentId : 5d8ae7040d32eb000d70f8a0
         * speak : {"text":"今年是2019年","type":"text"}
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
             * response : {"nlyear":"己亥","year":2019,"zodiac":"猪","after":{"year":2079}}
             * name : default
             * text : 2019
             * type : text
             */

            private String widgetName;
            private String duiWidget;
            private ResponseBean response;
            private String name;
            private int text;
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

            public ResponseBean getResponse() {
                return response;
            }

            public void setResponse(ResponseBean response) {
                this.response = response;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getText() {
                return text;
            }

            public void setText(int text) {
                this.text = text;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public static class ResponseBean {
                /**
                 * nlyear : 己亥
                 * year : 2019
                 * zodiac : 猪
                 * after : {"year":2079}
                 */

                private String nlyear;
                private int year;
                private String zodiac;
                private AfterBean after;

                public String getNlyear() {
                    return nlyear;
                }

                public void setNlyear(String nlyear) {
                    this.nlyear = nlyear;
                }

                public int getYear() {
                    return year;
                }

                public void setYear(int year) {
                    this.year = year;
                }

                public String getZodiac() {
                    return zodiac;
                }

                public void setZodiac(String zodiac) {
                    this.zodiac = zodiac;
                }

                public AfterBean getAfter() {
                    return after;
                }

                public void setAfter(AfterBean after) {
                    this.after = after;
                }

                public static class AfterBean {
                    /**
                     * year : 2079
                     */

                    private int year;

                    public int getYear() {
                        return year;
                    }

                    public void setYear(int year) {
                        this.year = year;
                    }
                }
            }
        }

        public static class SpeakBean {
            /**
             * text : 今年是2019年
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
