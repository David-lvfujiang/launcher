package com.fenda.calendar.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/10/10
 * @Describe: 查询年龄json实体
 */
public class QueryAgeBean {

    /**
     * recordId : 67f68d8399eb4fd88d21bf9e3edb6b10
     * skillId : 2019031800001161
     * requestId : 67f68d8399eb4fd88d21bf9e3edb6b10
     * nlu : {"skillId":"2019031800001161","res":"5d8ae7040d32eb000d70f883","input":"属狗的是哪一年出生的","inittime":19.781005859375,"loadtime":21.622802734375,"skill":"日历","skillVersion":"38","source":"dui","systime":201.44018554688,"semantics":{"request":{"slots":[{"pos":[1,1],"rawvalue":"狗","name":"生肖","rawpinyin":"gou","value":"狗"},{"name":"intent","value":"查询年龄"}],"task":"日历","confidence":1,"slotcount":2,"rules":{"5d8ae7040d32eb000d70fb4f":{"生肖":["狗",1,1],"intent":["查询年龄",-1,-1]}}}},"version":"2019.1.15.20:40:58","timestamp":1570687736}
     * dm : {"input":"属狗的是哪一年出生的","shouldEndSession":true,"task":"日历","widget":{"widgetName":"default","duiWidget":"text","extra":{"year":2018,"content":"2018年，1岁，2006年，13岁，1994年，25岁"},"name":"default","text":"2018年，1岁，2006年，13岁，1994年，25岁","type":"text"},"intentName":"查询年龄","runSequence":"nlgFirst","nlg":"生肖是狗的年份有，2018年，1岁，2006年，13岁，1994年，25岁","intentId":"5d8ae7040d32eb000d70f8a1","speak":{"text":"生肖是狗的年份有，2018年，1岁，2006年，13岁，1994年，25岁","type":"text"},"command":{"api":""},"taskId":"5d8ae7040d32eb000d70f883","status":1}
     * contextId : 7841921bb381445c8add644ce15d958f
     * sessionId : 7841921bb381445c8add644ce15d958f
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
         * input : 属狗的是哪一年出生的
         * inittime : 19.781005859375
         * loadtime : 21.622802734375
         * skill : 日历
         * skillVersion : 38
         * source : dui
         * systime : 201.44018554688
         * semantics : {"request":{"slots":[{"pos":[1,1],"rawvalue":"狗","name":"生肖","rawpinyin":"gou","value":"狗"},{"name":"intent","value":"查询年龄"}],"task":"日历","confidence":1,"slotcount":2,"rules":{"5d8ae7040d32eb000d70fb4f":{"生肖":["狗",1,1],"intent":["查询年龄",-1,-1]}}}}
         * version : 2019.1.15.20:40:58
         * timestamp : 1570687736
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
             * request : {"slots":[{"pos":[1,1],"rawvalue":"狗","name":"生肖","rawpinyin":"gou","value":"狗"},{"name":"intent","value":"查询年龄"}],"task":"日历","confidence":1,"slotcount":2,"rules":{"5d8ae7040d32eb000d70fb4f":{"生肖":["狗",1,1],"intent":["查询年龄",-1,-1]}}}
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
                 * slots : [{"pos":[1,1],"rawvalue":"狗","name":"生肖","rawpinyin":"gou","value":"狗"},{"name":"intent","value":"查询年龄"}]
                 * task : 日历
                 * confidence : 1
                 * slotcount : 2
                 * rules : {"5d8ae7040d32eb000d70fb4f":{"生肖":["狗",1,1],"intent":["查询年龄",-1,-1]}}
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
                     * 5d8ae7040d32eb000d70fb4f : {"生肖":["狗",1,1],"intent":["查询年龄",-1,-1]}
                     */

                    @SerializedName("5d8ae7040d32eb000d70fb4f")
                    private _$5d8ae7040d32eb000d70fb4fBean _$5d8ae7040d32eb000d70fb4f;

                    public _$5d8ae7040d32eb000d70fb4fBean get_$5d8ae7040d32eb000d70fb4f() {
                        return _$5d8ae7040d32eb000d70fb4f;
                    }

                    public void set_$5d8ae7040d32eb000d70fb4f(_$5d8ae7040d32eb000d70fb4fBean _$5d8ae7040d32eb000d70fb4f) {
                        this._$5d8ae7040d32eb000d70fb4f = _$5d8ae7040d32eb000d70fb4f;
                    }

                    public static class _$5d8ae7040d32eb000d70fb4fBean {
                        private List<String> 生肖;
                        private List<String> intent;

                        public List<String> get生肖() {
                            return 生肖;
                        }

                        public void set生肖(List<String> 生肖) {
                            this.生肖 = 生肖;
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
                     * pos : [1,1]
                     * rawvalue : 狗
                     * name : 生肖
                     * rawpinyin : gou
                     * value : 狗
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
         * input : 属狗的是哪一年出生的
         * shouldEndSession : true
         * task : 日历
         * widget : {"widgetName":"default","duiWidget":"text","extra":{"year":2018,"content":"2018年，1岁，2006年，13岁，1994年，25岁"},"name":"default","text":"2018年，1岁，2006年，13岁，1994年，25岁","type":"text"}
         * intentName : 查询年龄
         * runSequence : nlgFirst
         * nlg : 生肖是狗的年份有，2018年，1岁，2006年，13岁，1994年，25岁
         * intentId : 5d8ae7040d32eb000d70f8a1
         * speak : {"text":"生肖是狗的年份有，2018年，1岁，2006年，13岁，1994年，25岁","type":"text"}
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
             * extra : {"year":2018,"content":"2018年，1岁，2006年，13岁，1994年，25岁"}
             * name : default
             * text : 2018年，1岁，2006年，13岁，1994年，25岁
             * type : text
             */

            private String widgetName;
            private String duiWidget;
            private ExtraBean extra;
            private String name;
            private String text;
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

            public static class ExtraBean {
                /**
                 * year : 2018
                 * content : 2018年，1岁，2006年，13岁，1994年，25岁
                 */

                private int year;
                private String content;

                public int getYear() {
                    return year;
                }

                public void setYear(int year) {
                    this.year = year;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }
            }
        }

        public static class SpeakBean {
            /**
             * text : 生肖是狗的年份有，2018年，1岁，2006年，13岁，1994年，25岁
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
