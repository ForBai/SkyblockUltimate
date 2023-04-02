package me.anemoi.sbultimate.utils;


import net.minecraft.util.Vec3;

import java.util.List;

public class Trace {
    private String name;
    private int index;
    private Vec3 pos;
    private List<TracePos> trace;
    //private DimensionType type;

    public Trace(int index,
                 String name,
                 //DimensionType type,
                 Vec3 pos,
                 List<TracePos> trace) {
        this.index = index;
        this.name = name;
        //this.type = type;
        this.pos = pos;
        this.trace = trace;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    //public DimensionType getType() {
    //    return this.type;
    //}

    //public void setType(DimensionType type) {
    //    this.type = type;
    //}

    public List<TracePos> getTrace() {
        return this.trace;
    }

    public void setTrace(List<TracePos> trace) {
        this.trace = trace;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vec3 getPos() {
        return this.pos;
    }

    public void setPos(Vec3 pos) {
        this.pos = pos;
    }

    public static class TracePos {
        private final Vec3 pos;
        private final Timer stopWatch = new Timer();
        private long time;

        public TracePos(Vec3 pos) {
            this.pos = pos;
            stopWatch.reset();
        }

        public TracePos(Vec3 pos, long time) {
            this.pos = pos;
            stopWatch.reset();
            this.time = time;
        }

        public Vec3 getPos() {
            return pos;
        }

        public boolean shouldRemoveTrace() {
            return stopWatch.passedMs(2000);
        }

        public long getTime() {
            return time;
        }
    }
}
